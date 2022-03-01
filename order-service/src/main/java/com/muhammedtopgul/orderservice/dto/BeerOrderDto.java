package com.muhammedtopgul.orderservice.dto;

import com.muhammedtopgul.orderservice.enumeration.BeerOrderStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BeerOrderDto extends BaseItem {

    public BeerOrderDto() {
    }

    @Builder
    public BeerOrderDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, UUID customerId, List<BeerOrderLineDto> beerOrderLines,
                        BeerOrderStatusEnum orderStatus, String orderStatusCallbackUrl, String customerRef) {
        super(id, version, createdDate, lastModifiedDate);
        this.customerId = customerId;
        this.beerOrderLines = beerOrderLines;
        this.orderStatus = orderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
        this.customerRef = customerRef;
    }

    private UUID customerId;
    private String customerRef;
    private List<BeerOrderLineDto> beerOrderLines;
    private BeerOrderStatusEnum orderStatus;
    private String orderStatusCallbackUrl;
}
