package com.muhammedtopgul.application.common.enumeration;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:04
 */

public enum BeerOrderStatusEnum {
    NEW,
    VALIDATED,
    VALIDATION_EXCEPTION,
    ALLOCATED,
    ALLOCATION_EXCEPTION,
    PENDING_INVENTORY,
    PICKED_UP,
    DELIVERED,
    DELIVERY_EXCEPTION;
}
