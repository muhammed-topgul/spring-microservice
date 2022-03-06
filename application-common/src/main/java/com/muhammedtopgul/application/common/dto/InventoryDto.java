package com.muhammedtopgul.application.common.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:57
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventoryDto {
    private UUID id;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private UUID beerId;
    private Integer quantityOnHand;
}
