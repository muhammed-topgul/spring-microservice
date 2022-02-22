package com.muhammedtopgul.inventoryservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:56
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class InventoryEntity extends BaseEntity{

    @Builder
    public InventoryEntity(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, UUID beerId,
                         String upc, Integer quantityOnHand) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerId = beerId;
        this.upc = upc;
        this.quantityOnHand = quantityOnHand;
    }

    private UUID beerId;
    private String upc;
    private Integer quantityOnHand = 0;
}
