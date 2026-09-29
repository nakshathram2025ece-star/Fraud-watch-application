package com.example.fraudwatch.controller;

import com.example.fraudwatch.entity.Rule;
import com.example.fraudwatch.repository.RuleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleRepository repository;

    public RuleController(RuleRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Rule create(@RequestBody Rule rule) {
        return repository.save(rule);
    }

    @GetMapping
    public List<Rule> getAll() {
        return repository.findAll();
    }
}
