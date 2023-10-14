package com.springframework.spring6reactivemongo.mapper;

import com.springframework.spring6reactivemongo.domain.Beer;
import com.springframework.spring6reactivemongo.domain.Customer;
import com.springframework.spring6reactivemongo.model.BeerDTO;
import com.springframework.spring6reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
