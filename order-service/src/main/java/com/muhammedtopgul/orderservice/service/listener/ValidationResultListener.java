package com.muhammedtopgul.orderservice.service.listener;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.event.ValidateOrderResultEvent;
import com.muhammedtopgul.orderservice.service.statemachine.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 17:17
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConstants.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(ValidateOrderResultEvent event) {
        final UUID beerOrderId = event.getOrderId();
        log.debug("Validation Result for Order Id: " + beerOrderId);
        beerOrderManager.processValidationResult(beerOrderId, event.getIsValid());
    }
}
