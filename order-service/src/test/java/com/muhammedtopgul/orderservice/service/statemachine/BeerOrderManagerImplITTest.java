package com.muhammedtopgul.orderservice.service.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.application.common.constant.test.CustomerRefConstants;
import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.event.AllocationFailureEvent;
import com.muhammedtopgul.application.common.event.DeallocateOrderRequestEvent;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import com.muhammedtopgul.orderservice.service.BeerOrderService;
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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 16:04
 */

@ExtendWith(WireMockExtension.class)
@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TastingRoomService.class)})
class BeerOrderManagerImplITTest {

    @Autowired
    private BeerOrderManager beerOrderManager;

    @Autowired
    private BeerOrderService beerOrderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private JmsTemplate jmsTemplate;

    private CustomerEntity testCustomer;

    private final UUID beerId = UUID.randomUUID();
    private final String upc = "12345";

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
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundOrder.getOrderStatus());
        });

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            BeerOrderLineEntity beerOrderLineEntity = foundOrder.getBeerOrderLines().iterator().next();
            assertEquals(beerOrderLineEntity.getOrderQuantity(), beerOrderLineEntity.getQuantityAllocated());
        });

        savedBeerOrderEntity = beerOrderService.findById(savedBeerOrderEntity.getId());

        assertNotNull(savedBeerOrderEntity);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrderEntity.getOrderStatus());
        savedBeerOrderEntity.getBeerOrderLines().forEach(beerOrderLineEntity -> {
            assertEquals(beerOrderLineEntity.getOrderQuantity(), beerOrderLineEntity.getQuantityAllocated());
        });
    }

    @Test
    void testNewToPickUp() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundOrder.getOrderStatus());
        });

        beerOrderManager.beerOrderPickedUp(savedBeerOrderEntity.getId());

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.PICKED_UP, foundOrder.getOrderStatus());
        });

        BeerOrderEntity pickedUpOrder = beerOrderService.findById(savedBeerOrderEntity.getId());
        assertEquals(BeerOrderStatusEnum.PICKED_UP, pickedUpOrder.getOrderStatus());
    }

    @Test
    void testFailedValidation() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();
        beerOrderEntity.setCustomerRef(CustomerRefConstants.FAIL_VALIDATION);

        beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.VALIDATION_EXCEPTION, foundOrder.getOrderStatus());
        });
    }

    @Test
    void testAllocationFailure() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();
        beerOrderEntity.setCustomerRef(CustomerRefConstants.FAIL_ALLOCATION);

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.ALLOCATION_EXCEPTION, foundOrder.getOrderStatus());
        });

        AllocationFailureEvent event = (AllocationFailureEvent) jmsTemplate.receiveAndConvert(JmsQueues.ALLOCATE_FAILURE_QUEUE);
        assertNotNull(event);
        assertThat(event.getOrderId()).isEqualTo(savedBeerOrderEntity.getId());
    }

    @Test
    void testPartialAllocation() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();
        beerOrderEntity.setCustomerRef(CustomerRefConstants.PARTIAL_ALLOCATION);

        beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.PENDING_INVENTORY, foundOrder.getOrderStatus());
        });
    }

    @Test
    void testValidationPendingToCancel() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();
        beerOrderEntity.setCustomerRef(CustomerRefConstants.DONT_VALIDATE);

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.VALIDATION_PENDING, foundOrder.getOrderStatus());
        });

        beerOrderManager.cancelOrder(savedBeerOrderEntity.getId());

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundOrder.getOrderStatus());
        });
    }

    @Test
    void testAllocationPendingToCancel() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();
        beerOrderEntity.setCustomerRef(CustomerRefConstants.DONT_ALLOCATE);

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.ALLOCATION_PENDING, foundOrder.getOrderStatus());
        });

        beerOrderManager.cancelOrder(savedBeerOrderEntity.getId());

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundOrder.getOrderStatus());
        });
    }

//    @Test
    void testAllocatedToCancel() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(upc).build();

        wireMockServer.stubFor(get("/api/v1/beer/by-upc/" + upc)
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrderEntity beerOrderEntity = this.createBeerOrder();

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundOrder.getOrderStatus());
        });

        beerOrderManager.cancelOrder(savedBeerOrderEntity.getId());

        await().untilAsserted(() -> {
            BeerOrderEntity foundOrder = beerOrderService.findById(beerOrderEntity.getId());
            assertEquals(BeerOrderStatusEnum.CANCELLED, foundOrder.getOrderStatus());
        });

        DeallocateOrderRequestEvent event = (DeallocateOrderRequestEvent) jmsTemplate.receiveAndConvert(JmsQueues.DEALLOCATE_ORDER_REQUEST_QUEUE);
        assertNotNull(event);
        assertThat(event.getBeerOrderDto().getId()).isEqualTo(savedBeerOrderEntity.getId());
    }


    public BeerOrderEntity createBeerOrder() {
        BeerOrderEntity beerOrderEntity = BeerOrderEntity.builder()
                .customer(testCustomer)
                .build();

        Set<BeerOrderLineEntity> lineEntities = new HashSet<>();
        lineEntities.add(
                BeerOrderLineEntity.builder()
                        .beerId(beerId)
                        .upc(upc)
                        .orderQuantity(1)
                        .beerOrder(beerOrderEntity)
                        .build());

        beerOrderEntity.setBeerOrderLines(lineEntities);

        return beerOrderEntity;
    }
}