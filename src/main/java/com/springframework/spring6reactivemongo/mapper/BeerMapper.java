package com.springframework.spring6reactivemongo.mapper;

import com.springframework.spring6reactivemongo.domain.Beer;
import com.springframework.spring6reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
