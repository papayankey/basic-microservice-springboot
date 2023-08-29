package com.github.papayankey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/info")
public class CustomerInfoController {
    private System.Logger logger = System.getLogger(CustomerInfoController.class.getName());
    private final List<Customer> customers = new ArrayList<>();

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/{id}")
    public Optional<Customer> customerInfo(@PathVariable(name = "id") int id) {
        logger.log(System.Logger.Level.INFO, "server port: " + serverPort + "; customer-id: " + id);
        return customers.stream()
                .filter(customer -> customer.id() == id)
                .findFirst();
    }

    @PostMapping
    public Customer addCustomer(@RequestBody CustomerRequest customerRequest) {
        int id = customers.size() + 1;
        var customer = new Customer(id, customerRequest.firstName(), customerRequest.lastName(), customerRequest.gender());
        customers.add(customer);
        return customer;
    }

    @GetMapping
    public Customers getCustomers() {
        return new Customers(customers);
    }
}
