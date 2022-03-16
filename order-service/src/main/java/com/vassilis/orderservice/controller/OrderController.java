package com.vassilis.orderservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vassilis.orderservice.dto.OrderDto;
import com.vassilis.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/orders", produces = APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderDto orderToCreateDto) {
        var createdOrder = orderService.createOrder(orderToCreateDto);
        return ResponseEntity.status(CREATED).body(createdOrder);
    }
}
