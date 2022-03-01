package com.muhammedtopgul.orderservice.enumeration;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 11:18
 */

public enum BeerOrderEventEnum {
    VALIDATE_ORDER,
    VALIDATION_PASSED,
    VALIDATION_FAILED,
    ALLOCATION_SUCCESS,
    ALLOCATION_NO_INVENTORY,
    ALLOCATION_FAILED,
    BEER_ORDER_PICKED_UP;
}
