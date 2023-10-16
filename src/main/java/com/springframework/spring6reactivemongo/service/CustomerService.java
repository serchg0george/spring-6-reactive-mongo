package com.springframework.spring6reactivemongo.service;

import com.springframework.spring6reactivemongo.domain.Customer;
import com.springframework.spring6reactivemongo.model.CustomerDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {

    Mono<CustomerDTO> findFirstByCustomerName(String name);

    Flux<CustomerDTO> listCustomer();

    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);

    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> getCustomerById(String customerId);

    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(String customerId);
}
