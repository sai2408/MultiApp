// Client for the shared REST contract. Vite proxies /api to the Express server.
const API = "/api/tasks";

async function request(url, options = {}) {
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) throw new Error(`${options.method || "GET"} ${url} failed: ${res.status}`);
  return res.status === 204 ? null : res.json();
}

export const listTasks = () => request(API);
export const createTask = (title) => request(API, { method: "POST", body: JSON.stringify({ title }) });
export const updateTask = (id, changes) =>
  request(`${API}/${id}`, { method: "PUT", body: JSON.stringify(changes) });
export const deleteTask = (id) => request(`${API}/${id}`, { method: "DELETE" });
