package com.github.papayankey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerServiceController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/validate/{id}")
    public ResponseEntity<CustomerResponse> checkValidation(@PathVariable(name = "id") String id) {
        boolean isValidated = Boolean.TRUE.equals(restTemplate.getForObject("http://customer-validation-service/validation/" + id, Boolean.class));
        if (!isValidated) {
            var data = new CustomerResponse("customer with id %s is not validated".formatted(id),
                    null, HttpStatus.BAD_REQUEST, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        var data = new CustomerResponse("customer with id %s is validated".formatted(id),
                null, HttpStatus.OK, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable(name = "id") String id) {
        var optionalCustomer = restTemplate.getForObject("http://customer-info-service/info/" + id, Optional.class);
        if (Objects.nonNull(optionalCustomer) && optionalCustomer.isPresent()) {
            var data = new CustomerResponse(optionalCustomer.get(), null, HttpStatus.OK, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        var data = new CustomerResponse(null, String.format("customer with id %s, does not exist", id), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomers() {
        var customers = restTemplate.getForObject("http://customer-info-service/info", Customers.class);
        if (Objects.nonNull(customers) && customers.data().isEmpty()) {
            return new ResponseEntity<>(new CustomerResponse(null, "no customers registered yet", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CustomerResponse(customers.data(), null, HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody CustomerRequest customerRequest) {
        var customer = restTemplate.postForObject("http://customer-info-service/info", customerRequest, Customer.class);
        if (Objects.nonNull(customer)) {
            var validationRequest = new ValidationRequest(customer.id(), "%s %s".formatted(customer.firstName(), customer.lastName()));
            restTemplate.postForObject("http://customer-validation-service/validation", validationRequest, Void.class);
            var data = new CustomerResponse(null, "customer registration successful", HttpStatus.CREATED, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        }
        var data = new CustomerResponse(null, "one or more required parameter is missing", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}