package com.muhammedtopgul.beerservice.repository;

import com.muhammedtopgul.beerservice.entity.BeerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:35
 */

@Repository
public interface BeerRepository extends PagingAndSortingRepository<BeerEntity, UUID> {
}
