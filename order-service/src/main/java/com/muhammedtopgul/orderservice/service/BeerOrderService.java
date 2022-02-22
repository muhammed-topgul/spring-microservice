package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.orderservice.dto.BeerOrderDto;
import com.muhammedtopgul.orderservice.pageable.BeerOrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:18
 */

public interface BeerOrderService {
    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    void pickupOrder(UUID customerId, UUID orderId);
}
