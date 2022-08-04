package com.example.payroll.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException (Long id) {
        super ("No order with id " + id);
    }
}
