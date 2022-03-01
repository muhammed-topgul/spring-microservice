package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.dto.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 24.02.2022 13:52
 */

public interface BeerService {

    Optional<BeerDto> findById(UUID uuid);

    Optional<BeerDto> findByUpc(String upc);
}
