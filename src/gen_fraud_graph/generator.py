# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Core generator — orchestrates account, transaction, and fraud generation."""

from __future__ import annotations

import csv
import os
import random
from concurrent.futures import ProcessPoolExecutor

import numpy as np
from tqdm import tqdm

from gen_fraud_graph.client import send_accounts_batch, send_transactions_batch
from gen_fraud_graph.config import Config
from gen_fraud_graph.embeddings import EmbeddingGenerator
from gen_fraud_graph.exporters import get_headers
from gen_fraud_graph.typologies import FraudRingGenerator

# ---------------------------------------------------------------------------
# Normal transaction descriptions
# ---------------------------------------------------------------------------

NORMAL_DESCRIPTIONS: list[str] = [
    "grocery store purchase",
    "salary deposit",
    "utility bill payment",
    "online subscription",
    "restaurant payment",
    "atm withdrawal",
    "peer to peer transfer",
    "insurance premium",
    "mortgage payment",
    "investment deposit",
]


# ---------------------------------------------------------------------------
# Workload planning helpers
# ---------------------------------------------------------------------------


def _split_workload(total: int, num_shards: int) -> list[tuple[int, int]]:
    """Split ``total`` rows across ``num_shards`` shards without dropping rows."""
    if num_shards <= 0:
        raise ValueError("num_shards must be greater than zero")

    base, remainder = divmod(total, num_shards)
    shards: list[tuple[int, int]] = []
    start = 0

    for shard_idx in range(num_shards):
        count = base + (1 if shard_idx < remainder else 0)
        shards.append((start, count))
        start += count

    return shards


# ---------------------------------------------------------------------------
# Worker functions (must be top-level for multiprocessing)
# ---------------------------------------------------------------------------


def _generate_accounts_chunk(
    worker_id: int,
    batch_id: int,
    start_id: int,
    count: int,
    provider: str,
    dim: int,
    output_dir: str,
    fmt: str = "csv",
) -> str:
    """Generate a chunk of account rows (called by ProcessPoolExecutor)."""
    random.seed(start_id)
    embedder = EmbeddingGenerator(provider, dim=dim)  # type: ignore[arg-type]

    acc_dir = os.path.join(output_dir, "accounts")
    os.makedirs(acc_dir, exist_ok=True)

    headers = get_headers("account", fmt)  # type: ignore[arg-type]
    csv_path = os.path.join(acc_dir, f"accounts_{worker_id}_{batch_id}.csv")

    # Resume support
    existing_rows = 0
    file_exists = os.path.exists(csv_path)
    if file_exists:
        with open(csv_path) as fh:
            existing_rows = sum(1 for _ in fh) - 1
        if existing_rows >= count:
            return f"Worker {worker_id} Batch {batch_id}: Skipped (already complete)"
        print(f"  Worker {worker_id} Batch {batch_id}: Resuming from row {existing_rows}")

    batch_size = 5_000
    with open(csv_path, "a", newline="") as fh:
        writer = csv.writer(fh)
        if not file_exists:
            writer.writerow(headers)

        for i in range(max(0, existing_rows), count, batch_size):
            chunk_count = min(batch_size, count - i)
            batch_texts: list[str] = []
            batch_rows: list[list] = []

            for j in range(chunk_count):
                uid = start_id + i + j
                aid = f"acc_{uid}"
                name = f"Customer_{uid}"
                batch_texts.append(name)

                row: list = [
                    aid,
                    name,
                    round(random.uniform(100, 100_000), 2),
                    round(random.uniform(0, 1), 4),
                    "2023-01-01",
                ]
                if fmt == "neptune":
                    row.insert(1, "Account")
                batch_rows.append(row)

            if fmt == "neptune":
                embeddings = embedder.generate(batch_texts)
                final_rows = []
                for idx, r in enumerate(batch_rows):
                    vec = embeddings[idx]
                    if isinstance(vec, np.ndarray):
                        vec = vec.tolist()
                    final_rows.append(r + [";".join(map(str, vec))])
            else:
                final_rows = batch_rows

            writer.writerows(final_rows)

            # --- INTEGRACIÓN JAVA: Envío por sub-lotes (500 en 500) ---
            payload_accounts = [
                {
                    "accountId": r[0],
                    "customerName": r[1],
                    "balance": r[2],
                    "riskScore": r[3],
                    "creationDate": r[4],
                }
                for r in batch_rows
            ]
            network_batch_size = 500
            for k in range(0, len(payload_accounts), network_batch_size):
                sub_batch = payload_accounts[k : k + network_batch_size]
                send_accounts_batch(sub_batch)
            # -----------------------------------------------------------

            if (i + chunk_count) % 50_000 == 0:
                print(f"  Worker {worker_id} Batch {batch_id}: {i + chunk_count} accounts written & sent")

    return f"Worker {worker_id} Batch {batch_id}: Generated {count} accounts"


def _generate_transactions_chunk(
    worker_id: int,
    batch_id: int,
    start_tx_id: int,
    count: int,
    total_accounts: int,
    provider: str,
    dim: int,
    output_dir: str,
    fmt: str = "csv",
) -> str:
    """Generate a chunk of transaction rows (called by ProcessPoolExecutor)."""
    random.seed(start_tx_id)
    embedder = EmbeddingGenerator(provider, dim=dim)  # type: ignore[arg-type]

    tx_dir = os.path.join(output_dir, "transactions")
    os.makedirs(tx_dir, exist_ok=True)

    headers = get_headers("transaction", fmt)  # type: ignore[arg-type]
    csv_path = os.path.join(tx_dir, f"transactions_{worker_id}_{batch_id}.csv")

    # Resume support
    existing_rows = 0
    file_exists = os.path.exists(csv_path)
    if file_exists:
        with open(csv_path) as fh:
            existing_rows = sum(1 for _ in fh) - 1
        if existing_rows >= count:
            return f"Worker {worker_id} Batch {batch_id}: Skipped (already complete)"
        print(f"  Worker {worker_id} Batch {batch_id}: Resuming from row {existing_rows}")

    embed_batch_size = 5_000
    with open(csv_path, "a", newline="") as fh:
        writer = csv.writer(fh)
        if not file_exists:
            writer.writerow(headers)

        for i in range(max(0, existing_rows), count, embed_batch_size):
            chunk_count = min(embed_batch_size, count - i)
            batch_texts: list[str] = []
            batch_rows: list[list] = []

            for j in range(chunk_count):
                tx_uid = start_tx_id + i + j
                src = f"acc_{random.randint(0, total_accounts - 1)}"
                dst = f"acc_{random.randint(0, total_accounts - 1)}"
                while src == dst:
                    dst = f"acc_{random.randint(0, total_accounts - 1)}"

                desc = random.choice(NORMAL_DESCRIPTIONS)
                batch_texts.append(desc)

                # --- MODIFICACIÓN: Inyección de probabilidad de fraude (5%) ---
                is_fraud_tx = random.random() < 0.05
                tx_amount = round(random.uniform(5000, 25000), 2) if is_fraud_tx else round(random.uniform(10, 500), 2)
                # -------------------------------------------------------------

                row: list = [
                    f"tx_{tx_uid}",
                    src,
                    dst,
                    tx_amount,
                    "2024-01-01T10:00:00",
                    desc,
                ]
                if fmt == "neptune":
                    row.insert(3, "TRANSFER")
                batch_rows.append((row, is_fraud_tx))

            embeddings = embedder.generate(batch_texts)

            final_rows: list[list] = []
            clean_batch_rows: list[list] = []
            fraud_flags: list[bool] = []

            for idx, (r, is_fraud_flag) in enumerate(batch_rows):
                fraud_flags.append(is_fraud_flag)
                clean_row = r.copy()
                if fmt == "neptune":
                    final_rows.append(clean_row)
                    clean_batch_rows.append(clean_row)
                else:
                    vec = embeddings[idx]
                    if isinstance(vec, np.ndarray):
                        vec = vec.tolist()
                    row_with_emb = clean_row + ["|".join(map(str, vec))]
                    final_rows.append(row_with_emb)
                    clean_batch_rows.append(clean_row)

            writer.writerows(final_rows)

            # --- INTEGRACIÓN JAVA: Envío por sub-lotes (500 en 500) ---
            payload_txs = [
                {
                    "txId": r[0],
                    "srcId": r[1],
                    "dstId": r[2],
                    "amount": r[3],
                    "timestamp": r[4],
                    "description": r[5],
                    "isFraud": fraud_flags[idx],
                }
                for idx, r in enumerate(clean_batch_rows)
            ]
            network_batch_size = 500
            for k in range(0, len(payload_txs), network_batch_size):
                sub_batch = payload_txs[k : k + network_batch_size]
                send_transactions_batch(sub_batch)
            # -----------------------------------------------------------

            if (i + chunk_count) % 50_000 == 0:
                print(
                    f"  Worker {worker_id} Batch {batch_id}: "
                    f"{i + chunk_count} transactions written & sent"
                )

    return f"Worker {worker_id} Batch {batch_id}: Generated {count} transactions"


# ---------------------------------------------------------------------------
# High-level orchestrator
# ---------------------------------------------------------------------------


class FraudGraphGenerator:
    """Orchestrates the full synthetic fraud-graph generation pipeline."""

    def __init__(self, config: Config) -> None:
        self.cfg = config

    def run(self, *, skip_accounts: bool = False) -> None:
        """Execute the three-phase generation pipeline."""
        cfg = self.cfg
        os.makedirs(cfg.output_dir, exist_ok=True)

        print("=" * 50)
        print("gen_fraud_graph — Synthetic Fraud Graph Generator")
        print("=" * 50)
        print(f"  Scale factor : {cfg.scale_factor}")
        print(f"  Accounts     : {cfg.num_accounts:,}")
        print(f"  Transactions : {cfg.num_transactions:,}")
        print(f"  Fraud rings  : {cfg.num_fraud_rings:,}")
        print(f"  Hardness     : {cfg.hardness}")
        print(f"  Format       : {cfg.output_format}")
        print(f"  Embedding    : {cfg.embedding_provider}")
        print(f"  Workers      : {cfg.workers}")
        print(f"  Compress     : {cfg.compress}")
        print(f"  Output       : {cfg.output_dir}")
        print("=" * 50)

        # Phase 1 — Accounts
        if not skip_accounts:
            self._generate_accounts()
        else:
            print("\n[Phase 1] Skipping accounts (--skip-accounts)")

        # Phase 2 — Transactions
        self._generate_transactions()

        # Phase 3 — Fraud rings
        self._generate_fraud()

        print("\nDone! All data generated and sent to Java backend.")

    # ------------------------------------------------------------------

    def _generate_accounts(self) -> None:
        cfg = self.cfg
        print("\n[Phase 1] Generating accounts...")

        shard_plan = _split_workload(cfg.num_accounts, cfg.workers * cfg.batches_per_worker)

        with ProcessPoolExecutor(max_workers=cfg.workers) as pool:
            futures = []
            for w in range(cfg.workers):
                for b in range(cfg.batches_per_worker):
                    global_idx = w * cfg.batches_per_worker + b
                    start_id, count = shard_plan[global_idx]
                    futures.append(
                        pool.submit(
                            _generate_accounts_chunk,
                            w,
                            b,
                            start_id,
                            count,
                            cfg.embedding_provider,
                            cfg.embedding_dim,
                            cfg.output_dir,
                            cfg.output_format,
                        )
                    )
            for f in tqdm(futures, total=len(futures), desc="Account batches"):
                f.result()

    def _generate_transactions(self) -> None:
        cfg = self.cfg
        print("\n[Phase 2] Generating transactions...")

        shard_plan = _split_workload(cfg.num_transactions, cfg.workers * cfg.batches_per_worker)

        with ProcessPoolExecutor(max_workers=cfg.workers) as pool:
            futures = []
            for w in range(cfg.workers):
                for b in range(cfg.batches_per_worker):
                    global_idx = w * cfg.batches_per_worker + b
                    start_id, count = shard_plan[global_idx]
                    futures.append(
                        pool.submit(
                            _generate_transactions_chunk,
                            w,
                            b,
                            start_id,
                            count,
                            cfg.num_accounts,
                            cfg.embedding_provider,
                            cfg.embedding_dim,
                            cfg.output_dir,
                            cfg.output_format,
                        )
                    )
            for f in tqdm(futures, total=len(futures), desc="Transaction batches"):
                f.result()

    def _generate_fraud(self) -> None:
        cfg = self.cfg
        print("\n[Phase 3] Generating fraud rings...")

        embedder = EmbeddingGenerator(cfg.embedding_provider, dim=cfg.embedding_dim)
        assert cfg.num_fraud_rings is not None
        fraud_gen = FraudRingGenerator(
            num_rings=cfg.num_fraud_rings,
            depth_range=cfg.fraud_ring_depth_range,
            amount_jitter=cfg.amount_jitter,
            ring_overlap=cfg.ring_overlap,
            decoy_ratio=cfg.decoy_ratio,
        )
        n_tx, _ = fraud_gen.generate(
            max_account_id=cfg.num_accounts,
            start_tx_id=cfg.num_transactions,
            embedder=embedder,
            output_dir=cfg.output_dir,
            fmt=cfg.output_format,
            compress=cfg.compress,
        )
        print(f"  Injected {n_tx:,} fraud transactions across {cfg.num_fraud_rings:,} rings")