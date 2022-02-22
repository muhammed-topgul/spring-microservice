package com.muhammedtopgul.orderservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:07
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BeerOrderLineDto extends BaseItem {

    public BeerOrderLineDto() {
    }

    @Builder
    public BeerOrderLineDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
                            String upc, String beerName, UUID beerId, Integer orderQuantity) {
        super(id, version, createdDate, lastModifiedDate);
        this.upc = upc;
        this.beerName = beerName;
        this.beerId = beerId;
        this.orderQuantity = orderQuantity;
    }

    private String upc;
    private String beerName;
    private UUID beerId;
    private Integer orderQuantity = 0;
}
