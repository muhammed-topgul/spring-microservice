package com.muhammedtopgul.orderservice.repository;

import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:15
 */

@Repository
public interface BeerOrderRepository  extends JpaRepository<BeerOrderEntity, UUID> {

    Page<BeerOrderEntity> findAllByCustomer(CustomerEntity customerEntity, Pageable pageable);

    List<BeerOrderEntity> findAllByOrderStatus(BeerOrderStatusEnum orderStatusEnum);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BeerOrderEntity findOneById(UUID id);
}
