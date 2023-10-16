package com.springframework.spring6reactivemongo.service;

import com.springframework.spring6reactivemongo.mapper.CustomerMapper;
import com.springframework.spring6reactivemongo.model.CustomerDTO;
import com.springframework.spring6reactivemongo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerDTO> findFirstByCustomerName(String name) {
        return customerRepository.findFirstByCustomerName(name)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Flux<CustomerDTO> listCustomer() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO.map(customerMapper::customerDtoToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    foundCustomer.setCustomerName(customerDTO.getCustomerName());

                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {

                    if (StringUtils.hasText(customerDTO.getCustomerName())) {
                        foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    }

                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(String customerId) {
        return customerRepository.deleteById(customerId);
    }
}
