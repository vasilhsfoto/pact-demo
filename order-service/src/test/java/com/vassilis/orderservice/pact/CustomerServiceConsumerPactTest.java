package com.vassilis.orderservice.pact;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.vassilis.orderservice.client.CustomerServiceClient;
import com.vassilis.orderservice.configuration.RestTemplateConfiguration;
import com.vassilis.orderservice.dto.CustomerDto;
import com.vassilis.orderservice.exception.UnprocessableEntityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
public class CustomerServiceConsumerPactTest {

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @MockBean
    private RestTemplateConfiguration.CustomerServiceProperties customerServicePropertiesMocked;

    @BeforeEach
    void setUp(MockServer mockServer) {
        Mockito.when(customerServicePropertiesMocked.getUri()).thenReturn(mockServer.getUrl() + "/api");
    }

    @Pact(consumer = "order-service", provider = "customer-service")
    public RequestResponsePact createPactWhenGetCustomerByIdSucceeds(PactDslWithProvider builder) {
        return builder.given("get-customer-by-id")
                .uponReceiving("A GET customer by ID ")
                .path("/api/customers/customerId1")
                .method("GET")
                .headers(Map.of("Accept", "application/json"))
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("""
                         {
                             "id": "customerId1",
                             "name": "Vassilis"
                         }
                        """)
                .toPact();
    }

    @Pact(consumer = "order-service", provider = "customer-service")
    public RequestResponsePact createPactWhenCustomerNotFound(PactDslWithProvider builder) {
        return builder.given("customer-not-found")
                .uponReceiving("A GET customer by ID is not found")
                .path("/api/customers/customer-not-found")
                .method("GET")
                .headers(Map.of("Accept", "application/json"))
                .willRespondWith()
                .status(404)
                .headers(Map.of("Content-Type", "application/json"))
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPactWhenGetCustomerByIdSucceeds")
    void whenGetCustomerByIdSucceeds() {
        var expectedCustomerDto = CustomerDto.builder()
                .id("customerId1")
                .name("Vassilis")
                .build();
        assertThat(customerServiceClient.getCustomerById("customerId1"))
                .isEqualTo(expectedCustomerDto);
    }

    @Test
    @PactTestFor(pactMethod = "createPactWhenCustomerNotFound")
    void whenCustomerNotFound() {
        assertThrows(UnprocessableEntityException.class,
                () -> customerServiceClient.getCustomerById("customer-not-found"));
    }
}
