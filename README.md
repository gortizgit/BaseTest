# BaseTest – Dual stack (Java + Python)

This repo contains two minimal, production‑ready test stacks:

- **java-testng-extent** → UI tests using **Selenium 4 + TestNG + Extent Reports**, Maven, TestNG suite, screenshots on failure.
- **python-pytest** → UI + API tests using **pytest + Selenium 4 + requests**, screenshots + HTML report.

Both are wired to **GitHub Actions**; each job uploads an HTML report artifact.

## Quickstart

### Python
```bash
cd python-pytest
python -m venv .venv && source .venv/bin/activate  # Windows: .venv\Scripts\activate
pip install -r requirements.txt
pytest -m "ui or api" --headless --browser chrome --html=reports/report.html --self-contained-html
```

### Java (Maven)
```bash
cd java-testng-extent
mvn -q test
```

Reports:
- Python: `python-pytest/reports/report.html`
- Java: `java-testng-extent/reports/extent.html`

## Structure
- `INTERVIEW_1MIN_SCRIPTS.md` – 1‑minute interview scripts (Selenium, SQL, API) ready to present.
- Each stack is self‑contained; extend by adding new Page Objects and tests.
