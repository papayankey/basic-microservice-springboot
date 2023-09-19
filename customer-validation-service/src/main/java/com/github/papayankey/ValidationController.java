package com.github.papayankey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/verify")
public class ValidationController {
    Logger logger = LoggerFactory.getLogger(ValidationController.class);
    private final Map<Integer, String> validatedCustomers = new HashMap<>();

    @GetMapping("/{id}")
    public boolean checkValidation(@PathVariable(name = "id") int id) {
        logger.info("Received request to validate customer with id: {}", id);
        var result = validatedCustomers.getOrDefault(id, "");
        return !result.isEmpty();
    }

    @PostMapping
    public void addValidation(@RequestBody ValidationRequest validationRequest) {
        validatedCustomers.putIfAbsent(validationRequest.id(), validationRequest.fullName());
    }
}
