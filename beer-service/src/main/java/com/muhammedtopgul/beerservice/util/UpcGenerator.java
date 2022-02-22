package com.muhammedtopgul.beerservice.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:38
 */

@UtilityClass
public class UpcGenerator {

    public static long generateUpc() {
        return new Random().nextInt(1_000_000_000) + 100_000_000;
    }
}
