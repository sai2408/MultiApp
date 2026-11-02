package com.multiapp.ktor

import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Serializable
data class Task(val id: Long, val title: String, val done: Boolean)

/** JSON body for create/update. Fields are nullable so PUT can be partial. */
@Serializable
data class TaskRequest(val title: String? = null, val done: Boolean? = null)

@Serializable
data class ApiError(val error: String)

/** Thread-safe in-memory store. Data resets when the server restarts. */
class TaskStore {
    private val tasks = ConcurrentHashMap<Long, Task>()
    private val nextId = AtomicLong(1)

    fun all(): List<Task> = tasks.values.sortedBy { it.id }

    fun create(title: String): Task =
        Task(id = nextId.getAndIncrement(), title = title, done = false).also { tasks[it.id] = it }

    fun update(id: Long, title: String?, done: Boolean?): Task? =
        tasks.computeIfPresent(id) { _, task ->
            task.copy(
                title = title?.trim()?.takeIf { it.isNotEmpty() } ?: task.title,
                done = done ?: task.done,
            )
        }

    fun delete(id: Long): Boolean = tasks.remove(id) != null
}
