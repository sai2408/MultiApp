package com.multiapp.spring.repository;

import com.multiapp.spring.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Thread-safe in-memory store. Data resets when the server restarts. */
@Repository
public class TaskRepository {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Task> findAll() {
        List<Task> all = new ArrayList<>(tasks.values());
        all.sort(Comparator.comparingLong(Task::getId));
        return all;
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task create(String title) {
        Task task = new Task(nextId.getAndIncrement(), title, false);
        tasks.put(task.getId(), task);
        return task;
    }

    public boolean delete(long id) {
        return tasks.remove(id) != null;
    }
}
