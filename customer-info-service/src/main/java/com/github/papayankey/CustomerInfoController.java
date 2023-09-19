package com.github.papayankey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/info")
public class CustomerInfoController {
    private final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);
    private final List<Customer> customers = new ArrayList<>();

    @GetMapping("/{id}")
    public Optional<Customer> customerInfo(@PathVariable(name = "id") int id) {
        logger.info("Request to retrieve customer info with id: {}", id);
        return customers.stream()
                .filter(customer -> customer.id() == id)
                .findFirst();
    }

    @PostMapping
    public Customer addCustomer(@RequestBody CustomerRequest customerRequest) {
        logger.info("Request to add a customer with name: {} {}", customerRequest.firstName(), customerRequest.lastName());
        int id = customers.size() + 1;
        var customer = new Customer(id, customerRequest.firstName(), customerRequest.lastName(), customerRequest.gender());
        customers.add(customer);
        return customer;
    }

    @GetMapping
    public Customers getCustomers() {
        logger.info("Request to get all customers information from database");
        return new Customers(customers);
    }
}
