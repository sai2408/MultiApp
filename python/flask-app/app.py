import sqlite3

from flask import Flask, g, jsonify, redirect, render_template, request, url_for

DB_PATH = "tasks.db"

app = Flask(__name__)


# ---------- database ----------

def get_db():
    if "db" not in g:
        g.db = sqlite3.connect(DB_PATH)
        g.db.row_factory = sqlite3.Row
    return g.db


@app.teardown_appcontext
def close_db(_exc):
    conn = g.pop("db", None)
    if conn is not None:
        conn.close()


def init_db():
    with sqlite3.connect(DB_PATH) as conn:
        conn.execute(
            "CREATE TABLE IF NOT EXISTS tasks ("
            " id INTEGER PRIMARY KEY AUTOINCREMENT,"
            " title TEXT NOT NULL,"
            " done INTEGER NOT NULL DEFAULT 0)"
        )


def to_dict(row):
    return {"id": row["id"], "title": row["title"], "done": bool(row["done"])}


def find_task(task_id):
    return get_db().execute("SELECT * FROM tasks WHERE id = ?", (task_id,)).fetchone()


def all_tasks():
    return get_db().execute("SELECT * FROM tasks ORDER BY id").fetchall()


def insert_task(title):
    db = get_db()
    cur = db.execute("INSERT INTO tasks (title) VALUES (?)", (title,))
    db.commit()
    return find_task(cur.lastrowid)


def save_task(task_id, title, done):
    db = get_db()
    db.execute("UPDATE tasks SET title = ?, done = ? WHERE id = ?", (title, int(done), task_id))
    db.commit()
    return find_task(task_id)


def remove_task(task_id):
    db = get_db()
    cur = db.execute("DELETE FROM tasks WHERE id = ?", (task_id,))
    db.commit()
    return cur.rowcount > 0


# ---------- REST API (shared contract) ----------

@app.after_request
def allow_cors(response):
    if request.path.startswith("/api/"):
        response.headers["Access-Control-Allow-Origin"] = "*"
        response.headers["Access-Control-Allow-Headers"] = "Content-Type"
        response.headers["Access-Control-Allow-Methods"] = "GET, POST, PUT, DELETE, OPTIONS"
    return response


@app.get("/api/tasks")
def api_list():
    return jsonify([to_dict(r) for r in all_tasks()])


@app.post("/api/tasks")
def api_create():
    title = str((request.get_json(silent=True) or {}).get("title", "")).strip()
    if not title:
        return jsonify(error="title is required"), 400
    return jsonify(to_dict(insert_task(title))), 201


@app.put("/api/tasks/<int:task_id>")
def api_update(task_id):
    task = find_task(task_id)
    if task is None:
        return jsonify(error="not found"), 404
    data = request.get_json(silent=True) or {}
    title = str(data.get("title", task["title"])).strip() or task["title"]
    done = bool(data.get("done", task["done"]))
    return jsonify(to_dict(save_task(task_id, title, done)))


@app.delete("/api/tasks/<int:task_id>")
def api_delete(task_id):
    if not remove_task(task_id):
        return jsonify(error="not found"), 404
    return "", 204


# ---------- server-rendered web UI ----------

@app.get("/")
def index():
    return render_template("index.html", tasks=[to_dict(r) for r in all_tasks()])


@app.post("/add")
def add():
    title = request.form.get("title", "").strip()
    if title:
        insert_task(title)
    return redirect(url_for("index"))


@app.post("/toggle/<int:task_id>")
def toggle(task_id):
    task = find_task(task_id)
    if task is not None:
        save_task(task_id, task["title"], not task["done"])
    return redirect(url_for("index"))


@app.post("/delete/<int:task_id>")
def delete(task_id):
    remove_task(task_id)
    return redirect(url_for("index"))


if __name__ == "__main__":
    init_db()
    app.run(port=5001, debug=True)
