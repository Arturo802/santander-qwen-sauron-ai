# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Embedding generators for node/edge feature vectors (Local-only)."""

from __future__ import annotations

from collections.abc import Sequence
from typing import Literal

import numpy as np


class EmbeddingGenerator:
    """Generate dense vector embeddings for text descriptions locally.

    Supports two backends:
    * ``"fake"`` — fast random vectors (no extra dependencies).
    * ``"local"`` — ``sentence-transformers`` with ``all-MiniLM-L6-v2``.
    """

    def __init__(
        self,
        provider: Literal["fake", "local"] = "fake",
        dim: int = 768,
    ) -> None:
        self.provider = provider
        self.dim = dim
        self._model = None

        if provider == "local":
            try:
                from sentence_transformers import SentenceTransformer
            except ImportError as exc:
                raise ImportError(
                    "sentence-transformers is not installed. "
                    "Install with: pip install sentence-transformers. "
                    "Alternatively, use provider='fake' (random vectors)."
                ) from exc

            print("Loading local SentenceTransformer model...")
            self._model = SentenceTransformer("all-MiniLM-L6-v2")

    def generate(self, texts: Sequence[str]) -> list[list[float]] | np.ndarray:
        """Return embeddings for a batch of *texts*."""
        if not texts:
            return []

        if self.provider == "fake":
            return np.random.rand(len(texts), self.dim).astype("float32")

        if self.provider == "local":
            return self._model.encode(texts)  # type: ignore[no-any-return,union-attr]

        return []