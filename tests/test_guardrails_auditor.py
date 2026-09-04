# Copyright (c) 2026 Santander Group
# SPDX-License-Identifier: Apache-2.0

"""Unit tests for Sauron AI Guardrails and Meta-Auditor."""

from __future__ import annotations
import pytest
import sys
import os

# Agregamos la ruta raíz al path para que Python encuentre los submódulos correctamente
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../../src')))

from ai_python.qwen_integration.guardrails_auditor import (
    OutputGuardrail,
    MetaAuditorWatcher,
)

def test_output_guardrail_safe_payload():
    payload = {"status": "success", "processed_records": 45}
    assert OutputGuardrail.sanitize_output(payload) is True

def test_output_guardrail_exfiltration_blocked():
    # Payload con intentos de exfiltración a IPs externas
    malicious_payload = {"data": "export to http://malicious-external-server.com/steal"}
    assert OutputGuardrail.sanitize_output(malicious_payload) is False

    
def test_meta_auditor_watcher_behavior():
    auditor = MetaAuditorWatcher(failure_threshold=2)
    
    # Primer pico masivo (Contador pasa a 1/2, aún retorna True)
    assert auditor.audit_agent_metrics("DataSentinel", anomalies_detected=60) is True
    
    # Segundo pico masivo consecutivo (Contador llega a 2/2, activa lockdown y retorna False)
    assert auditor.audit_agent_metrics("DataSentinel", anomalies_detected=75) is False