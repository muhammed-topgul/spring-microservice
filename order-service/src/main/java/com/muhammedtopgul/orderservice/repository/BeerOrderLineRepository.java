package com.muhammedtopgul.orderservice.repository;

import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:14
 */

@Repository
public interface BeerOrderLineRepository extends PagingAndSortingRepository<BeerOrderLineEntity, UUID> {
}
