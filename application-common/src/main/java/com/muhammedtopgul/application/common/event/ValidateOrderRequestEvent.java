package com.muhammedtopgul.application.common.event;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 15:53
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderRequestEvent {

    private BeerOrderDto beerOrderDto;
}
