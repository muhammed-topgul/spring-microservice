package com.muhammedtopgul.application.common.event;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 14:28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateOrderResultEvent {

    private BeerOrderDto beerOrderDto;
    private Boolean allocationError = false;
    private Boolean pendingInventory = false;
}
