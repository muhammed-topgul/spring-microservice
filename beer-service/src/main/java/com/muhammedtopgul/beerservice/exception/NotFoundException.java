package com.muhammedtopgul.beerservice.exception;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:24
 */

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
