package com.multiapp.spring.web;

import com.multiapp.spring.model.Task;
import com.multiapp.spring.model.TaskRequest;
import com.multiapp.spring.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/** REST API (shared contract). */
@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {

    private final TaskRepository repository;

    public TaskApiController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> list() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskRequest body) {
        if (body.title() == null || body.title().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "title is required"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.create(body.title().trim()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody TaskRequest body) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(task -> {
                    if (body.title() != null && !body.title().isBlank()) {
                        task.setTitle(body.title().trim());
                    }
                    if (body.done() != null) {
                        task.setDone(body.done());
                    }
                    return ResponseEntity.ok(task);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return repository.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found"));
    }
}
