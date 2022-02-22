package com.muhammedtopgul.beerservice.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author muhammed-topgul
 * @since 21.02.2022 23:00
 */

@AllArgsConstructor
@Getter
public enum BeerName {

    SNOW("Snow"),
    BUD_LIGHT("Bud Light"),
    SKOL("Skol"),
    BUDWEISER("Budweiser"),
    HARBIN("Harbin");

    private final String name;
}
