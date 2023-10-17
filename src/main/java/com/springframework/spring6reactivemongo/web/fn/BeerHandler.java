package com.springframework.spring6reactivemongo.web.fn;

import com.springframework.spring6reactivemongo.model.BeerDTO;
import com.springframework.spring6reactivemongo.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    public Mono<ServerResponse> createBeer(ServerRequest request) {
        return beerService.saveBeer(request.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(beerService.getById(request.pathVariable("beerId")), BeerDTO.class);
    }

    public Mono<ServerResponse> listBeer(ServerRequest request) {
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }
}
