package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.pageable.BeerOrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:18
 */

public interface BeerOrderService {

    BeerOrderPagedList findAll(UUID customerId, Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    BeerOrderEntity findById(UUID uuid);

    void pickupOrder(UUID customerId, UUID orderId);
}
