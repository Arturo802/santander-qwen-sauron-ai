# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""HTTP client to send generated accounts and transactions to the Java backend."""

from __future__ import annotations

import requests

BACKEND_URL = "http://localhost:8080/api/ingest"


def send_accounts_batch(accounts: list[dict]) -> bool:
    """Send a batch of accounts to the Spring Boot backend."""
    if not accounts:
        return True
    try:
        response = requests.post(f"{BACKEND_URL}/accounts", json=accounts, timeout=120)
        if response.status_code == 200:
            return True
        else:
            print(f"  [Warning] Failed to ingest accounts batch: {response.text}")
            return False
    except requests.exceptions.ConnectionError:
        print("  [Error] Could not connect to Java backend at http://localhost:8080. Is it running?")
        return False
    except requests.exceptions.Timeout:
        print("  [Error] Request timed out while sending accounts batch to Java backend.")
        return False


def send_transactions_batch(transactions: list[dict]) -> bool:
    """Send a batch of transactions to the Spring Boot backend."""
    if not transactions:
        return True
    try:
        response = requests.post(f"{BACKEND_URL}/transactions", json=transactions, timeout=120)
        if response.status_code == 200:
            return True
        else:
            print(f"  [Warning] Failed to ingest transactions batch: {response.text}")
            return False
    except requests.exceptions.ConnectionError:
        print("  [Error] Could not connect to Java backend at http://localhost:8080. Is it running?")
        return False
    except requests.exceptions.Timeout:
        print("  [Error] Request timed out while sending transactions batch to Java backend.")
        return False