package com.github.papayankey;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {
    private List<Customer> customers = new ArrayList<>(
            List.of(
                    new Customer(1, "customer-1"),
                    new Customer(2, "customer-2")
            )
    );

    @GetMapping("/{id}")
    public Customer customerInfo(@PathVariable(name = "id") int id) {
        return customers.stream()
                .filter(customer -> customer.id() == id)
                .findFirst()
                .orElseGet(() -> new Customer(id));
    }

    @PostMapping
    public CustomersWrapper addCustomer(@RequestBody Customer customer) {
        customers.add(customer);
        return new CustomersWrapper(customers);
    }

    record CustomersWrapper(List<Customer> customers) {
    }
}
