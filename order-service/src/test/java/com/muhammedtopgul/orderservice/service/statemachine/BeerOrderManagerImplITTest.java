package com.muhammedtopgul.orderservice.service.statemachine;

import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 16:04
 */

@SpringBootTest
class BeerOrderManagerImplITTest {

    @Autowired
    private BeerOrderManager beerOrderManager;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerEntity testCustomer;

    private final UUID beerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(
                CustomerEntity.builder()
                        .customerName("Test Customer")
                        .build());
    }

    @Test
    void testNewToAllocated() {
        BeerOrderEntity beerOrderEntity = this.createBeerOrder();

        BeerOrderEntity savedBeerOrderEntity = beerOrderManager.newBeerOrderEntity(beerOrderEntity);

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
                        .orderQuantity(1)
                        .beerOrder(beerOrderEntity)
                        .build());

        beerOrderEntity.setBeerOrderLines(lineEntities);

        return beerOrderEntity;
    }
}