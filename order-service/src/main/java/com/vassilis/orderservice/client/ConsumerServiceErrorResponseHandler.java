package com.vassilis.orderservice.client;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.vassilis.orderservice.exception.UnprocessableEntityException;

@Component
public class ConsumerServiceErrorResponseHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new UnprocessableEntityException("Customer was not found in system");
        }

        var errorMsg = new String(response.getBody().readAllBytes());

        throw new RuntimeException("Error calling the consumer-service api: " + errorMsg);
    }
}
