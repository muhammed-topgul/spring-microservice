package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.enumeration.BeerStyle;
import com.muhammedtopgul.beerservice.pageable.BeerPagedList;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:28
 */

public interface BeerService {

    BeerPagedList findAll(String beerName, BeerStyle beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto findById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto save(BeerDto beerDto);

    BeerDto update(UUID beerId, BeerDto beerDto);

    BeerDto findByUpc(String upc);
}
