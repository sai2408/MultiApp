# MERN Task Manager

**M**ongoDB + **E**xpress + **R**eact + **N**ode.

- `server/` — Express 5 + Mongoose, the shared API on port 5004. A counter collection gives tasks numeric ids
  so the contract matches every other backend.
- `client/` — React (Vite) on port 5173; Vite proxies `/api` to the server.

Requires Node 18+ and a MongoDB server (local install, Docker, or a MongoDB Atlas connection string).

```bash
# optional: run MongoDB in Docker
docker run -d -p 27017:27017 --name mongo mongo:7

# terminal 1 — API
cd server
cp .env.example .env          # edit MONGODB_URI if you use Atlas
npm install
npm run dev

# terminal 2 — React app
cd client
npm install
npm run dev
```

- Web UI: http://localhost:5173
- API: http://localhost:5004/api/tasks
