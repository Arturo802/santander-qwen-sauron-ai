# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Configuration for the synthetic fraud graph generator."""

from dataclasses import dataclass, field
from typing import Literal

Hardness = Literal["low", "medium", "high"]

_HARDNESS_PRESETS: dict[str, dict[str, float]] = {
    "low": {"amount_jitter": 0.0, "ring_overlap": 0.0, "decoy_ratio": 0.0},
    "medium": {"amount_jitter": 0.25, "ring_overlap": 0.25, "decoy_ratio": 0.5},
    "high": {"amount_jitter": 0.5, "ring_overlap": 0.5, "decoy_ratio": 1.0},
}


@dataclass
class Config:
    """Generator configuration (Local & Zero-OpenAI version)."""

    scale_factor: float = 1.0
    num_fraud_rings: int | None = None
    fraud_ring_depth_range: tuple[int, int] = (4, 7)
    # Únicamente permitimos proveedores locales o falsos
    embedding_provider: Literal["fake", "local"] = "fake"
    embedding_dim: int = 768
    workers: int = 1
    batches_per_worker: int = 1
    output_format: Literal["csv", "neptune"] = "csv"
    compress: bool = False
    output_dir: str = "data"
    hardness: Hardness = "low"

    # Derived — computed in __post_init__
    num_accounts: int = field(init=False)
    num_transactions: int = field(init=False)
    amount_jitter: float = field(init=False)
    ring_overlap: float = field(init=False)
    decoy_ratio: float = field(init=False)

    def __post_init__(self) -> None:
        self.num_accounts = int(10_000_000 * self.scale_factor)
        self.num_transactions = int(90_000_000 * self.scale_factor)
        if self.num_fraud_rings is None:
            self.num_fraud_rings = max(10, int(1000 * self.scale_factor))

        if self.hardness not in _HARDNESS_PRESETS:
            raise ValueError(
                f"hardness must be one of {sorted(_HARDNESS_PRESETS)}, got {self.hardness!r}"
            )
        preset = _HARDNESS_PRESETS[self.hardness]
        self.amount_jitter = preset["amount_jitter"]
        self.ring_overlap = preset["ring_overlap"]
        self.decoy_ratio = preset["decoy_ratio"]