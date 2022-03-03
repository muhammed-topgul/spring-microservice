package com.muhammedtopgul.orderservice.service.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import com.muhammedtopgul.orderservice.service.scheduled.TastingRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 16:04
 */

@ExtendWith(WireMockExtension.class)
@SpringBootTest
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TastingRoomService.class)})
class BeerOrderManagerImplITTest {

    @Autowired
    private BeerOrderManager beerOrderManager;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WireMockServer wireMockServer;

    private CustomerEntity testCustomer;

    private final UUID beerId = UUID.randomUUID();

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8083));
            wireMockServer.start();
            return wireMockServer;
        }
    }

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(
                CustomerEntity.builder()
                        .customerName("Test Customer")
                        .build());
    }

    @Test
    void testNewToAllocated() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc("12345").build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/12345")
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderRepository.findById(beerOrderEntity.getId()).get();

            // TODO Allocated Status
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundOrder.getOrderStatus());
        });

        savedBeerOrderEntity = beerOrderRepository.findById(savedBeerOrderEntity.getId()).get();

        assertNotNull(savedBeerOrderEntity);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrderEntity.getOrderStatus());
    }

    public BeerOrderEntity createBeerOrder() {
        BeerOrderEntity beerOrderEntity = BeerOrderEntity.builder()
                .customer(testCustomer)
                .build();

        Set<BeerOrderLineEntity> lineEntities = new HashSet<>();
        lineEntities.add(
                BeerOrderLineEntity.builder()
                        .beerId(beerId)
                        .upc("12345")
                        .orderQuantity(1)
                        .beerOrder(beerOrderEntity)
                        .build());

        beerOrderEntity.setBeerOrderLines(lineEntities);

        return beerOrderEntity;
    }
}