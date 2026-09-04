# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Tests for gen_fraud_graph.embeddings (fake and local providers only)."""

from __future__ import annotations

import sys
import types

import pytest

from gen_fraud_graph.embeddings import EmbeddingGenerator


class TestEmbeddings:
    def test_fake_provider_shape(self):
        emb = EmbeddingGenerator("fake", dim=128)
        result = emb.generate(["hello", "world"])
        assert result.shape == (2, 128)

    def test_fake_provider_empty(self):
        emb = EmbeddingGenerator("fake")
        result = emb.generate([])
        assert result == []

    def test_fake_provider_deterministic_shape(self):
        emb = EmbeddingGenerator("fake", dim=768)
        texts = [f"text_{i}" for i in range(100)]
        result = emb.generate(texts)
        assert result.shape == (100, 768)


class TestEmbeddingsProviders:
    def test_local_import_error(self, monkeypatch):
        monkeypatch.setitem(sys.modules, "sentence_transformers", None)
        with pytest.raises(ImportError, match="sentence-transformers is not installed"):
            EmbeddingGenerator("local")

    def test_local_generate(self, monkeypatch):
        import numpy as np

        fake_st = types.ModuleType("sentence_transformers")

        class _ST:
            def __init__(self, name):
                self.name = name

            def encode(self, texts):
                return np.zeros((len(texts), 4))

        fake_st.SentenceTransformer = _ST
        monkeypatch.setitem(sys.modules, "sentence_transformers", fake_st)
        emb = EmbeddingGenerator("local")
        result = emb.generate(["a", "b"])
        assert result.shape == (2, 4)