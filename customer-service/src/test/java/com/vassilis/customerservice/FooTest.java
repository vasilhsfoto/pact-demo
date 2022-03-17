package com.vassilis.customerservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("pact")
public class FooTest {

    @Value("${pactbroker.host}")
    private String pactHost;

    @Test
    public void foo() {
        System.out.println(pactHost);
/*
        Optional<String> opt = Optional.empty();

        String plain = opt.orElseGet(() -> "it was empty");
        System.out.println(plain);
*/
    }
}
