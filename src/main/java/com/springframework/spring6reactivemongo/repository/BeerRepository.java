package com.springframework.spring6reactivemongo.repository;

import com.springframework.spring6reactivemongo.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveCrudRepository<Beer, String> {

    Mono<Beer> findFirstByBeerName(String name);

}
