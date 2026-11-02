# Django Task Manager

Django ORM model + Django templates for the UI, plus plain `JsonResponse` views for the shared API.

```bash
python -m venv .venv
.venv\Scripts\activate        # macOS/Linux: source .venv/bin/activate
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver 5002
```

- Web UI: http://localhost:5002
- API: http://localhost:5002/api/tasks
