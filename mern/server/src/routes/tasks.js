import { Router } from "express";
import Task from "../models/Task.js";

// REST API (shared contract)
const router = Router();

// Non-numeric ids can't match any task; treat them as not found.
const parseId = (raw) => (/^\d+$/.test(raw) ? Number(raw) : -1);

router.get("/", async (_req, res) => {
  res.json(await Task.find().sort({ id: 1 }));
});

router.post("/", async (req, res) => {
  const title = String(req.body?.title ?? "").trim();
  if (!title) return res.status(400).json({ error: "title is required" });
  const task = await Task.create({ title });
  res.status(201).json(task);
});

router.put("/:id", async (req, res) => {
  const task = await Task.findOne({ id: parseId(req.params.id) });
  if (!task) return res.status(404).json({ error: "not found" });

  const { title, done } = req.body ?? {};
  if (typeof title === "string" && title.trim()) task.title = title.trim();
  if (typeof done === "boolean") task.done = done;
  await task.save();
  res.json(task);
});

router.delete("/:id", async (req, res) => {
  const { deletedCount } = await Task.deleteOne({ id: parseId(req.params.id) });
  if (!deletedCount) return res.status(404).json({ error: "not found" });
  res.status(204).end();
});

export default router;
