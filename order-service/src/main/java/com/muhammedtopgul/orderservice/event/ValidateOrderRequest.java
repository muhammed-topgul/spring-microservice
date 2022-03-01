package com.muhammedtopgul.orderservice.event;

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
public class ValidateOrderRequest {

    private BeerOrderDto beerOrderDto;
}
