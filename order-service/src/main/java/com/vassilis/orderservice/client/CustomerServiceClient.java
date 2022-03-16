package com.vassilis.orderservice.client;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vassilis.orderservice.dto.CustomerDto;
import lombok.RequiredArgsConstructor;

import static com.vassilis.orderservice.configuration.RestTemplateConfiguration.CustomerServiceProperties;

@Service
@RequiredArgsConstructor
public class CustomerServiceClient {
    private final RestTemplate customerServiceRestTemplate;
    private final CustomerServiceProperties properties;

    public CustomerDto getCustomerById(String customerId) {
        var getCustomerUri =
                UriComponentsBuilder.fromUriString(properties.getUri())
                        .pathSegment("customers", "{id}")
                        .build(customerId);

        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return customerServiceRestTemplate.exchange(getCustomerUri, HttpMethod.GET, entity, CustomerDto.class)
                .getBody();
    }
}
