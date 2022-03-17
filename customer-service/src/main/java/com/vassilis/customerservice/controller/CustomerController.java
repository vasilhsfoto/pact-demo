package com.vassilis.customerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vassilis.customerservice.dto.CustomerDto;
import com.vassilis.customerservice.exception.ResourceNotFoundException;
import com.vassilis.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/customers", produces = APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public CustomerDto find(@PathVariable String customerId) {
        return customerService.getCustomer(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Customer with %s not found", customerId)));
    }
}
