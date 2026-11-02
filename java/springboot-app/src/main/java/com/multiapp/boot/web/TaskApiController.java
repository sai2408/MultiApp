package com.multiapp.boot.web;

import com.multiapp.boot.model.Task;
import com.multiapp.boot.model.TaskRequest;
import com.multiapp.boot.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskApiController {

    private final TaskService service;

    public TaskApiController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskRequest body) {
        if (body.title() == null || body.title().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "title is required"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(body.title()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody TaskRequest body) {
        return service.update(id, body.title(), body.done())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "not found"));
    }
}
