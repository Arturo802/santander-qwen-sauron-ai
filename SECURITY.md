# Security Policy

## Reporting a Vulnerability

If you discover a security vulnerability in this project, please report it responsibly. **Do not open a public GitHub issue for security vulnerabilities.**

### How to Report

Please use **GitHub's private vulnerability reporting** as the primary channel:

1. **GitHub Private Vulnerability Reporting** (preferred): open the repository's **Security** tab → **Report a vulnerability**, or use [this link](https://github.com/Arturo802/santander-qwen-sauron-ai/security/advisories/new). This creates a private advisory visible only to you and the maintainers.

### What to Include

- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if any)

### Response SLA

| Stage | SLA |
|:---|:---|
| Acknowledgment of report | < 48 hours |
| Initial assessment and severity classification | < 7 days |
| Fix for Critical/High severity | < 30 days |
| Fix for Medium/Low severity | < 90 days |

## Scope

This security policy applies **only** to code in this repository.

## Security Best Practices for Contributors

- Never commit secrets, API keys, tokens, or credentials
- Never commit internal URLs, IP addresses, or corporate email addresses
- Never commit personally identifiable information (PII) or customer data
- Use environment variables for any configuration that could be sensitive
- Keep dependencies up to date