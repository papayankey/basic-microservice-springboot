package com.github.papayankey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/validate/{id}")
    public String checkValidation(@PathVariable(name = "id") String id) {
        boolean isValidated = restTemplate.getForObject(
                "http://customer-validation-service/validation/" + id, Boolean.class);
        if (isValidated) {
            return "Customer is validated and can be registered";
        }
        return "Customer validation failed";
    }

    @GetMapping("/{id}")
    public Customer customerInfo(@PathVariable(name = "id") String id) {
        Customer customer = restTemplate.getForObject("http://customer-info-service/info/" + id, Customer.class);
        return customer;
    }

    @PostMapping
    public CustomerWrapper addCustomer(@RequestBody Customer customer) {
        CustomerWrapper response = restTemplate.postForObject(
                "http://customer-info-service/info", customer, CustomerWrapper.class);
        return response;
    }
}
