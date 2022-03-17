package com.vassilis.customerservice.pact;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.vassilis.customerservice.dto.CustomerDto;
import com.vassilis.customerservice.service.CustomerService;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("customer-service")
@Consumer("order-service")
@ActiveProfiles("pact")
@PactFolder("pacts")
@PactBroker
class VerificationForOrderServicePactTest {

    @MockBean
    private CustomerService customerServiceMocked;

    @LocalServerPort
    private int port;

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State("get-customer-by-id")
    void getCustomerByIdState() {
        var customerDtoReturned = CustomerDto.builder()
                .id("customerId1")
                .name("Vassilis")
                .money("dollars")
                .dateOfBirth(LocalDate.of(1981, 10, 12))
                .build();
        when(customerServiceMocked.getCustomer("customerId1")).thenReturn(Optional.of(customerDtoReturned));
    }

    @State("customer-not-found")
    void customerNotFoundState() {
        when(customerServiceMocked.getCustomer("customerId1")).thenReturn(Optional.empty());
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void verifyGetCustomerReturned(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
