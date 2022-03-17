package com.vassilis.customerservice.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CustomerDto {
    String id;
    String name;
    LocalDate dateOfBirth;
    String money;
}
