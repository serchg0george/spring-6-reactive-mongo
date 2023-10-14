package com.springframework.spring6reactivemongo.service;

import com.springframework.spring6reactivemongo.domain.Beer;
import com.springframework.spring6reactivemongo.model.BeerDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface BeerService {
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> getById(String beerId);
}
