package com.muhammedtopgul.beerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 23.02.2022 16:02
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {

    private UUID id;
    private UUID beerId;
    private Integer quantityOnHand;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
}