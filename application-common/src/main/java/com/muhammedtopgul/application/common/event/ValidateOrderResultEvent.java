package com.muhammedtopgul.application.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 16:54
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderResultEvent {

    private UUID orderId;
    private Boolean isValid;
}
