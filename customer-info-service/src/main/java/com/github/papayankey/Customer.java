package com.github.papayankey;

public record Customer(int id, String name) {
    public Customer(int id) {
        this(id, "customer does not exist!");
    }
}
