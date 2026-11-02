# FastAPI Task Manager

API-first: FastAPI + SQLModel (SQLite) for the shared API, and a static HTML page that calls it with `fetch`.

```bash
python -m venv .venv
.venv\Scripts\activate        # macOS/Linux: source .venv/bin/activate
pip install -r requirements.txt
python main.py                # or: uvicorn main:app --port 5003 --reload
```

- Web UI: http://localhost:5003
- API: http://localhost:5003/api/tasks
- Swagger docs: http://localhost:5003/docs
