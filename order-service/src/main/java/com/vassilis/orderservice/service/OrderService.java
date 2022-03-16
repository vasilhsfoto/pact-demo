package com.vassilis.orderservice.service;

import java.util.LinkedList;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vassilis.orderservice.client.CustomerServiceClient;
import com.vassilis.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerServiceClient customerServiceClient;

    public OrderDto createOrder(OrderDto orderToCreateDto) {
        var customerDto = customerServiceClient.getCustomerById(orderToCreateDto.getCustomerId());

        return OrderDto.builder()
                .id(UUID.randomUUID().toString())
                .customerId(orderToCreateDto.getCustomerId())
                .orderItems(new LinkedList<>(orderToCreateDto.getOrderItems()))
                .build();
    }
}
