package com.github.papayankey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation")
public class Validation {
    @GetMapping("/{id}")
    public boolean customer(@PathVariable(name = "id") int id) {
        return id == 1 || id == 2;
    }
}
