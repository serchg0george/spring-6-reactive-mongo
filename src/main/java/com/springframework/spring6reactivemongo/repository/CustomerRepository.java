package com.springframework.spring6reactivemongo.repository;

import com.springframework.spring6reactivemongo.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {

    Flux<Customer> findByCustomerName(String name);

    Mono<Customer> findFirstByCustomerName(String name);

}
