package com.muhammedtopgul.orderservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:00
 */

@Getter
@Setter
//@NoArgsConstructor
@Entity
public class BeerOrderLineEntity extends BaseEntity {

    public BeerOrderLineEntity() {
    }

    @Builder
    public BeerOrderLineEntity(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate,
                         BeerOrderEntity beerOrder, UUID beerId, Integer orderQuantity,
                         Integer quantityAllocated) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerOrder = beerOrder;
        this.beerId = beerId;
        this.orderQuantity = orderQuantity;
        this.quantityAllocated = quantityAllocated;
    }

    @ManyToOne
    private BeerOrderEntity beerOrder;

    private UUID beerId;
    private Integer orderQuantity = 0;
    private Integer quantityAllocated = 0;
}