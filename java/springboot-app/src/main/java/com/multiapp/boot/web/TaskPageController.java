package com.multiapp.boot.web;

import com.multiapp.boot.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Server-rendered web UI (Thymeleaf). */
@Controller
public class TaskPageController {

    private final TaskService service;

    public TaskPageController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", service.findAll());
        model.addAttribute("stack", "Spring Boot");
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title) {
        if (!title.isBlank()) {
            service.create(title);
        }
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable long id) {
        service.toggle(id);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        service.delete(id);
        return "redirect:/";
    }
}
