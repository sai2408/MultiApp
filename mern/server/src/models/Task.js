import mongoose from "mongoose";

// Mongo's _id is an ObjectId string, but the shared contract uses numeric ids,
// so a counter document hands out sequential ids.
const counterSchema = new mongoose.Schema({
  _id: String,
  seq: { type: Number, default: 0 },
});
const Counter = mongoose.model("Counter", counterSchema);

const taskSchema = new mongoose.Schema({
  id: { type: Number, unique: true, index: true },
  title: { type: String, required: true, trim: true },
  done: { type: Boolean, default: false },
});

taskSchema.pre("save", async function assignId() {
  if (this.isNew && this.id == null) {
    const counter = await Counter.findByIdAndUpdate(
      "tasks",
      { $inc: { seq: 1 } },
      { new: true, upsert: true },
    );
    this.id = counter.seq;
  }
});

// Serialize as { id, title, done } — hide Mongo's _id and __v.
taskSchema.set("toJSON", {
  transform: (_doc, ret) => ({ id: ret.id, title: ret.title, done: ret.done }),
});

export default mongoose.model("Task", taskSchema);
