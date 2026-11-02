package com.multiapp.boot.service;

import com.multiapp.boot.model.Task;
import com.multiapp.boot.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    public Task create(String title) {
        return repository.save(new Task(title.trim()));
    }

    /** Applies only the non-null fields; empty if the task does not exist. */
    public Optional<Task> update(long id, String title, Boolean done) {
        return repository.findById(id).map(task -> {
            if (title != null && !title.isBlank()) {
                task.setTitle(title.trim());
            }
            if (done != null) {
                task.setDone(done);
            }
            return task;
        });
    }

    public Optional<Task> toggle(long id) {
        return repository.findById(id).map(task -> {
            task.setDone(!task.isDone());
            return task;
        });
    }

    public boolean delete(long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
