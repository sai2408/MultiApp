import { useEffect, useState } from "react";
import { createTask, deleteTask, listTasks, updateTask } from "./api.js";

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState("");
  const [error, setError] = useState(null);

  // Runs an API call, then reloads the list; failures show in the banner.
  async function run(action) {
    try {
      await action();
      setTasks(await listTasks());
      setError(null);
    } catch (e) {
      setError(e.message);
    }
  }

  useEffect(() => {
    run(async () => {});
  }, []);

  function handleAdd(e) {
    e.preventDefault();
    const trimmed = title.trim();
    if (!trimmed) return;
    setTitle("");
    run(() => createTask(trimmed));
  }

  return (
    <main>
      <h1>
        Tasks <small>MERN</small>
      </h1>

      <form className="add" onSubmit={handleAdd}>
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="What needs doing?"
          autoFocus
        />
        <button type="submit">Add</button>
      </form>

      {error && <p className="error">{error}</p>}

      <ul>
        {tasks.map((t) => (
          <li key={t.id} className={t.done ? "done" : ""}>
            <button onClick={() => run(() => updateTask(t.id, { done: !t.done }))}>
              {t.done ? "↺" : "✓"}
            </button>
            <span>{t.title}</span>
            <button onClick={() => run(() => deleteTask(t.id))}>Delete</button>
          </li>
        ))}
        {tasks.length === 0 && !error && (
          <li>
            <em>No tasks yet.</em>
          </li>
        )}
      </ul>
    </main>
  );
}
