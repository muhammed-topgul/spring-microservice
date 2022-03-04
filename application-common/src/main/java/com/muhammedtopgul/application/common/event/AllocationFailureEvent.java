package com.muhammedtopgul.application.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 04.03.2022 13:46
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocationFailureEvent {

    private UUID orderId;
}
