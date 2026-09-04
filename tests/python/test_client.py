# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Tests for gen_fraud_graph.client (HTTP communication with Java backend)."""

from __future__ import annotations

import pytest
import requests
from gen_fraud_graph.client import send_accounts_batch, send_transactions_batch


class MockResponse:
    def __init__(self, json_data, status_code):
        self.json_data = json_data
        self.status_code = status_code
        self.text = "OK"

    def json(self):
        return self.json_data


class TestClient:
    def test_send_accounts_empty(self):
        assert send_accounts_batch([]) is True

    def test_send_transactions_empty(self):
        assert send_transactions_batch([]) is True

    def test_send_accounts_success(self, monkeypatch):
        def mock_post(*args, **kwargs):
            return MockResponse({"status": "success"}, 200)

        monkeypatch.setattr(requests, "post", mock_post)
        
        data = [{"accountId": "acc_1", "customerName": "Test", "balance": 100.0, "riskScore": 0.1, "creationDate": "2023-01-01"}]
        assert send_accounts_batch(data) is True

    def test_send_transactions_success(self, monkeypatch):
        def mock_post(*args, **kwargs):
            return MockResponse({"status": "success"}, 200)

        monkeypatch.setattr(requests, "post", mock_post)
        
        data = [{"txId": "tx_1", "srcId": "acc_1", "dstId": "acc_2", "amount": 50.0, "timestamp": "2024-01-01T10:00:00", "description": "test", "isFraud": False}]
        assert send_transactions_batch(data) is True

    def test_connection_error_handling(self, monkeypatch):
        def mock_post(*args, **kwargs):
            raise requests.exceptions.ConnectionError("Refused")

        monkeypatch.setattr(requests, "post", mock_post)
        
        data = [{"accountId": "acc_1", "customerName": "Test", "balance": 100.0, "riskScore": 0.1, "creationDate": "2023-01-01"}]
        assert send_accounts_batch(data) is False