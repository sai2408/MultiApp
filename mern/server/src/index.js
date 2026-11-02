import "dotenv/config";
import cors from "cors";
import express from "express";
import mongoose from "mongoose";
import taskRoutes from "./routes/tasks.js";

const PORT = process.env.PORT || 5004;
const MONGODB_URI = process.env.MONGODB_URI || "mongodb://127.0.0.1:27017/multiapp_tasks";

const app = express();
app.use(cors());
app.use(express.json());

app.use("/api/tasks", taskRoutes);

// Express 5 forwards errors thrown in async handlers here.
app.use((err, _req, res, _next) => {
  console.error(err);
  res.status(500).json({ error: "internal error" });
});

await mongoose.connect(MONGODB_URI);
app.listen(PORT, () => console.log(`MERN API on http://localhost:${PORT}/api/tasks`));
