package com.muhammedtopgul.application.common.constant.jms;

import lombok.experimental.UtilityClass;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 11:41
 */

@UtilityClass
public class JmsQueues {

    public static final String BREWING_REQUEST_QUEUE = "brewing-request-queue";
    public static final String NEW_INVENTORY_QUEUE = "new-inventory-queue";
    public static final String VALIDATE_ORDER_REQUEST_QUEUE = "validate-order-request-queue";
    public static final String VALIDATE_ORDER_RESPONSE_QUEUE = "validate-order-response-queue";
    public static final String ALLOCATE_ORDER_REQUEST_QUEUE = "allocate-order-request-queue";
    public static final String ALLOCATE_ORDER_RESPONSE_QUEUE = "allocate-order-response-queue";
    public static final String ALLOCATE_FAILURE_QUEUE = "allocate-failure-queue";
    public static final String DEALLOCATE_ORDER_REQUEST_QUEUE = "allocate-order-request-queue";
}
