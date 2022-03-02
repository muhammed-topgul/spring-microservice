package com.muhammedtopgul.inventoryservice.service;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 14:48
 */

public interface AllocationService {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
