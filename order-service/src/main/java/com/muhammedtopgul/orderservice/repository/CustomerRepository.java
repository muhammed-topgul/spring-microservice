package com.muhammedtopgul.orderservice.repository;

import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:16
 */

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    List<CustomerEntity> findAllByCustomerNameLike(String customerName);
}
