package com.vassilis.customerservice.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vassilis.customerservice.dto.CustomerDto;

@Service
public class CustomerService {
    private static final Map<String, CustomerDto> customerIdToCustomer;

    static {
        customerIdToCustomer = new HashMap<>();
        var vassilis = CustomerDto.builder()
                .id("customerId1")
                .dateOfBirth(LocalDate.of(1981, 10, 10))
                .name("Vassilis")
                .build();

        var dana = CustomerDto.builder()
                .id("customerId2")
                .dateOfBirth(LocalDate.of(1983, 10, 10))
                .name("Dana")
                .build();

        customerIdToCustomer.put("customerId1", vassilis);
        customerIdToCustomer.put("customerId2", dana);
    }

    public Optional<CustomerDto> getCustomer(String customerId) {
        return Optional.ofNullable(customerIdToCustomer.get(customerId));
    }
}
