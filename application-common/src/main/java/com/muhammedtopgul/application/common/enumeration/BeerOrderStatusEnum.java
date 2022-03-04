package com.muhammedtopgul.application.common.enumeration;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:04
 */

public enum BeerOrderStatusEnum {
    NEW,
    VALIDATION_PENDING,
    VALIDATED,
    VALIDATION_EXCEPTION,
    ALLOCATION_PENDING,
    ALLOCATED,
    ALLOCATION_EXCEPTION,
    CANCELLED,
    PENDING_INVENTORY,
    PICKED_UP,
    DELIVERED,
    DELIVERY_EXCEPTION;
}
