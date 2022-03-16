package com.vassilis.orderservice.configuration;

import java.time.Duration;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import com.vassilis.orderservice.client.ConsumerServiceErrorResponseHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestTemplateConfiguration {

    @Bean
    public RestTemplateCustomizer customRestTemplateCustomizer(ClientHttpRequestInterceptor logClientRequestInterceptor) {
        return restTemplate -> {
            restTemplate.getInterceptors().add(logClientRequestInterceptor);
        };
    }

    @Bean
    public ClientHttpRequestInterceptor logClientRequestInterceptor() {
        return (request, body, execution) -> {
            log.info("request {} on {}", request.getMethod(), request.getURI());
            return execution.execute(request, body);
        };
    }

    @Bean
    public RestTemplate customerServiceRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                    ConsumerServiceErrorResponseHandler consumerServiceErrorResponseHandler) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(3))
                .errorHandler(consumerServiceErrorResponseHandler)
                .build();
    }

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "customer-service")
    public static class CustomerServiceProperties {
        @NotBlank
        private String uri;
    }
}