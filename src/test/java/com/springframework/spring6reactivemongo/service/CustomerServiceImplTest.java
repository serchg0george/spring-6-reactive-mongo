package com.springframework.spring6reactivemongo.service;

import com.springframework.spring6reactivemongo.domain.Customer;
import com.springframework.spring6reactivemongo.mapper.CustomerMapper;
import com.springframework.spring6reactivemongo.mapper.CustomerMapperImpl;
import com.springframework.spring6reactivemongo.model.CustomerDTO;
import com.springframework.spring6reactivemongo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CustomerRepository customerRepository;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    @Test
    @DisplayName("Test Save Customer Using Subscriber")
    void testSaveCustomerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDto = new AtomicReference<>();

        Mono<CustomerDTO> savedCustomer = customerService.saveCustomer(Mono.just(customerDTO));

        savedCustomer.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        await().untilTrue(atomicBoolean);

        CustomerDTO persistDto = atomicDto.get();
        assertThat(persistDto).isNotNull();
        assertThat(persistDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Customer Using Block")
    void testSaveCustomerUseBlock() {
        CustomerDTO savedDto = customerService.saveCustomer(Mono.just(getTestCustomerDto())).block();
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getId()).isNotNull();
    }


    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateCustomerStreaming() {
        final String newCustomerName = "New Customer Name";

        AtomicReference<CustomerDTO> atomicDto = new AtomicReference<>();

        customerService.saveCustomer(Mono.just(getTestCustomerDto()))
                .map(savedCustomerDto -> {
                    savedCustomerDto.setCustomerName(newCustomerName);
                    return savedCustomerDto;
                })
                .flatMap(customerService::saveCustomer)
                .flatMap(saveUpdatedDto -> customerService.getCustomerById(saveUpdatedDto.getId()))
                .subscribe(atomicDto::set);

        await().until(() -> atomicDto.get() != null);
        assertThat(atomicDto.get().getCustomerName()).isEqualTo(newCustomerName);
    }


    @Test
    void testDeleteCustomerById() {
        CustomerDTO customerToDelete = getSavedCustomerDto();

        customerService.deleteCustomerById(customerToDelete.getId()).block();

        Mono<CustomerDTO> expectedEmptyCustomerMono = customerService.getCustomerById(customerToDelete.getId());

        CustomerDTO emptyCustomer = expectedEmptyCustomerMono.block();

        assertThat(emptyCustomer).isNull();
    }

    public CustomerDTO getSavedCustomerDto() {
        return customerService.saveCustomer(Mono.just(getTestCustomerDto())).block();
    }

    public static CustomerDTO getTestCustomerDto() {
        return new CustomerMapperImpl().customerToCustomerDto(getTestCustomer());
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("My first customer")
                .build();
    }
}