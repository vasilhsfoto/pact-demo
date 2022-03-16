package com.vassilis.orderservice.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OrderDto {
    String id;
    @NotBlank
    String customerId;
    @NotEmpty
    List<String> orderItems;
}
