package com.springframework.spring6reactivemongo.web.fn;

import com.springframework.spring6reactivemongo.domain.Customer;
import com.springframework.spring6reactivemongo.model.BeerDTO;
import com.springframework.spring6reactivemongo.model.CustomerDTO;
import com.springframework.spring6reactivemongo.service.CustomerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPatchIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchIdFound() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateCustomerBadRequest() {
        CustomerDTO testCustomer = getSavedTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerServiceImplTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        CustomerDTO customerDTO = getSavedTestCustomer();
        customerDTO.setCustomerName("New Customer");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateCustomerBadData() {
        Customer testCustomer = CustomerServiceImplTest.getTestCustomer();
        testCustomer.setCustomerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO testDto = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testDto), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetById() {
        CustomerDTO customerDTO = getSavedTestCustomer();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testCustomerByName() {
        final String CUSTOMER_NAME = "TEST";
        CustomerDTO testDto = getSavedTestCustomer();
        testDto.setCustomerName(CUSTOMER_NAME);

        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testDto), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange();

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(UriComponentsBuilder
                        .fromPath(CustomerRouterConfig.CUSTOMER_PATH)
                        .queryParam("customerName", CUSTOMER_NAME).build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(equalTo(1));
    }

    @Test
    @Order(2)
    void testListCustomers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    public CustomerDTO getSavedTestCustomer() {
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getRequestHeaders().get("Location");

        return webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange().returnResult(CustomerDTO.class).getResponseBody().blockFirst();
    }
}