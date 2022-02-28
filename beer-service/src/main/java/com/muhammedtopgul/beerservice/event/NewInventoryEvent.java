package com.muhammedtopgul.beerservice.event;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import lombok.NoArgsConstructor;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:39
 */

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
