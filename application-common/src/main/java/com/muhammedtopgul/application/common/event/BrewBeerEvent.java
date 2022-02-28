package com.muhammedtopgul.application.common.event;

import com.muhammedtopgul.application.common.dto.BeerDto;
import lombok.NoArgsConstructor;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:39
 */

@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent {

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
