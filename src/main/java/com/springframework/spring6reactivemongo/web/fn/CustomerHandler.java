package com.springframework.spring6reactivemongo.web.fn;

import com.springframework.spring6reactivemongo.model.CustomerDTO;
import com.springframework.spring6reactivemongo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(customerService.getCustomerById(request.pathVariable("customerId")), CustomerDTO.class);
    }

    public Mono<ServerResponse> listCustomer(ServerRequest request) {
        return ServerResponse.ok()
                .body(customerService.listCustomer(), CustomerDTO.class);
    }
}
