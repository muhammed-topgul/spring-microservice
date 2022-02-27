package com.muhammedtopgul.beerservice.repository;

import com.muhammedtopgul.beerservice.entity.BeerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:35
 */

@Repository
public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {

    Page<BeerEntity> findAllByBeerNameAndBeerStyle(String name, String style, PageRequest pageRequest);

    Page<BeerEntity> findAllByBeerName(String name, PageRequest pageRequest);

    Page<BeerEntity> findAllByBeerStyle(String style, PageRequest pageRequest);

    BeerEntity findByUpc(String upc);
}
