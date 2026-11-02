from contextlib import asynccontextmanager
from pathlib import Path
from typing import Optional

from fastapi import Depends, FastAPI, HTTPException, Response
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse, JSONResponse
from fastapi.exceptions import RequestValidationError
from sqlmodel import Field, Session, SQLModel, create_engine, select

engine = create_engine("sqlite:///tasks.db", connect_args={"check_same_thread": False})


# ---------- models ----------

class Task(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    title: str
    done: bool = False


class TaskCreate(SQLModel):
    title: str = ""


class TaskUpdate(SQLModel):
    title: Optional[str] = None
    done: Optional[bool] = None


# ---------- app setup ----------

@asynccontextmanager
async def lifespan(_app: FastAPI):
    SQLModel.metadata.create_all(engine)
    yield


app = FastAPI(title="Task Manager · FastAPI", lifespan=lifespan)
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])


@app.exception_handler(RequestValidationError)
async def bad_request(_request, _exc):
    return JSONResponse({"error": "invalid request"}, status_code=400)


@app.exception_handler(HTTPException)
async def http_error(_request, exc: HTTPException):
    return JSONResponse({"error": exc.detail}, status_code=exc.status_code)


def get_session():
    with Session(engine) as session:
        yield session


def get_task_or_404(task_id: int, session: Session) -> Task:
    task = session.get(Task, task_id)
    if task is None:
        raise HTTPException(status_code=404, detail="not found")
    return task


# ---------- REST API (shared contract) ----------

@app.get("/api/tasks", response_model=list[Task])
def list_tasks(session: Session = Depends(get_session)):
    return session.exec(select(Task).order_by(Task.id)).all()


@app.post("/api/tasks", response_model=Task, status_code=201)
def create_task(body: TaskCreate, session: Session = Depends(get_session)):
    title = body.title.strip()
    if not title:
        return JSONResponse({"error": "title is required"}, status_code=400)
    task = Task(title=title)
    session.add(task)
    session.commit()
    session.refresh(task)
    return task


@app.put("/api/tasks/{task_id}", response_model=Task)
def update_task(task_id: int, body: TaskUpdate, session: Session = Depends(get_session)):
    task = get_task_or_404(task_id, session)
    if body.title is not None and body.title.strip():
        task.title = body.title.strip()
    if body.done is not None:
        task.done = body.done
    session.add(task)
    session.commit()
    session.refresh(task)
    return task


@app.delete("/api/tasks/{task_id}", status_code=204)
def delete_task(task_id: int, session: Session = Depends(get_session)):
    session.delete(get_task_or_404(task_id, session))
    session.commit()
    return Response(status_code=204)


# ---------- frontend (static page that calls the API) ----------

@app.get("/", include_in_schema=False)
def index():
    return FileResponse(Path(__file__).parent / "static" / "index.html")


if __name__ == "__main__":
    import uvicorn

    # Pass the app object (not "main:app" + reload): reload re-imports this file under a
    # second module name, which would define the Task table twice. For auto-reload use:
    #   uvicorn main:app --port 5003 --reload
    uvicorn.run(app, port=5003)
