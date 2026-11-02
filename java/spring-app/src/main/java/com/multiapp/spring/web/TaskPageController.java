package com.multiapp.spring.web;

import com.multiapp.spring.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Server-rendered web UI (Thymeleaf). */
@Controller
public class TaskPageController {

    private final TaskRepository repository;

    public TaskPageController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", repository.findAll());
        model.addAttribute("stack", "Spring MVC");
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title) {
        if (!title.isBlank()) {
            repository.create(title.trim());
        }
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable long id) {
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        repository.delete(id);
        return "redirect:/";
    }
}
