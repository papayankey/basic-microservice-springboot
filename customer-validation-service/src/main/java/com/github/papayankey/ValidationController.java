package com.github.papayankey;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validation")
public class ValidationController {
    private final Map<Integer, String> validatedCustomers = new HashMap<>();

    @GetMapping("/{id}")
    public boolean checkValidation(@PathVariable(name = "id") int id) {
        var result = validatedCustomers.getOrDefault(id, "");
        return !result.isEmpty();
    }

    @PostMapping
    public void addValidation(@RequestBody ValidationRequest validationRequest) {
        validatedCustomers.putIfAbsent(validationRequest.id(), validationRequest.fullName());
    }
}
