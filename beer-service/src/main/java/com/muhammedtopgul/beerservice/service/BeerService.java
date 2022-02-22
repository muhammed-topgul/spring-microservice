package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.beerservice.dto.BeerDto;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:28
 */

public interface BeerService {

    BeerDto findById(UUID beerId);

    BeerDto save(BeerDto beerDto);

    BeerDto update(UUID beerId, BeerDto beerDto);
}
