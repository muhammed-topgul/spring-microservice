package com.muhammedtopgul.orderservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    public BeerOrderLineDto(UUID id,
                            Integer version,
                            OffsetDateTime createdDate,
                            OffsetDateTime lastModifiedDate,
                            UUID beerId,
                            String beerName,
                            String beerStyle,
                            String upc,
                            Integer orderQuantity,
                            BigDecimal price) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerId = beerId;
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.upc = upc;
        this.orderQuantity = orderQuantity;
        this.price = price;
    }

    private String upc;
    private String beerName;
    private String beerStyle;
    private UUID beerId;
    private Integer orderQuantity = 0;
    private BigDecimal price;
}
