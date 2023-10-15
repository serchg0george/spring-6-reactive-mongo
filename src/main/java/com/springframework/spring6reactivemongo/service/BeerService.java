package com.springframework.spring6reactivemongo.service;

import com.springframework.spring6reactivemongo.model.BeerDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface BeerService {
    Flux<BeerDTO> listBeers();
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDto);

    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);
    Mono<BeerDTO> getById(String beerId);

    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeerById(String beerId);
}
