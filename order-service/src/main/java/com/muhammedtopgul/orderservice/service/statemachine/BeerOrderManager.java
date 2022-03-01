package com.muhammedtopgul.orderservice.service.statemachine;

import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 15:00
 */

public interface BeerOrderManager {

    BeerOrderEntity newBeerOrderEntity(BeerOrderEntity beerOrderEntity);

    void processValidationResult(UUID beerOrderId, Boolean isValid);
}
