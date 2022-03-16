package com.vassilis.orderservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerDto {
    String id;
    String name;
}
