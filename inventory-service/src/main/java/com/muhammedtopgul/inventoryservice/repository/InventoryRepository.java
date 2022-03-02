package com.muhammedtopgul.inventoryservice.repository;

import com.muhammedtopgul.inventoryservice.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:58
 */

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    List<InventoryEntity> findAllByBeerId(UUID beerId);

    List<InventoryEntity> findAllByUpc(String upc);
}