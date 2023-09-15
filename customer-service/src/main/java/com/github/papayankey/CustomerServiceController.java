package com.github.papayankey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RefreshScope
@RestController
@RequestMapping("/customers")
public class CustomerServiceController {
    Logger logger = LoggerFactory.getLogger(CustomerServiceController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.customer-validation}")
    private String validationService;

    @Value("${services.customer-info}")
    private String infoService;

    @GetMapping("/verify/{id}")
    public ResponseEntity<CustomerResponse> checkValidation(@PathVariable(name = "id") String id) {
        logger.info("Making a request to validation info service with id: {}", id);
        boolean isValidated = Boolean.TRUE.equals(restTemplate.getForObject(validationService + id, Boolean.class));
        if (!isValidated) {
            var data = new CustomerResponse("customer %s is not validated".formatted(id),
                    null, HttpStatus.BAD_REQUEST, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        var data = new CustomerResponse("customer %s is validated".formatted(id),
                null, HttpStatus.OK, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable(name = "id") String id) {
        var optionalCustomer = restTemplate.getForObject(infoService + id, Optional.class);
        if (Objects.nonNull(optionalCustomer) && optionalCustomer.isPresent()) {
            var data = new CustomerResponse(optionalCustomer.get(), null, HttpStatus.OK, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        var data = new CustomerResponse(null, String.format("customer with id %s, does not exist", id), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomers() {
        var customers = restTemplate.getForObject(infoService, Customers.class);
        if (Objects.nonNull(customers) && customers.data().isEmpty()) {
            return new ResponseEntity<>(new CustomerResponse(null, "no customers registered yet", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CustomerResponse(customers.data(), null, HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@RequestBody CustomerRequest customerRequest) {
        var customer = restTemplate.postForObject(infoService, customerRequest, Customer.class);
        if (Objects.nonNull(customer)) {
            var validationRequest = new ValidationRequest(customer.id(), "%s %s".formatted(customer.firstName(), customer.lastName()));
            restTemplate.postForObject(validationService, validationRequest, Void.class);
            var data = new CustomerResponse(null, "customer registration successful", HttpStatus.CREATED, LocalDateTime.now());
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        }
        var data = new CustomerResponse(null, "one or more required parameter is missing", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
