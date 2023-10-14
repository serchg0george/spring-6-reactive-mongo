package com.springframework.spring6reactivemongo.repository;

import com.springframework.spring6reactivemongo.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}
