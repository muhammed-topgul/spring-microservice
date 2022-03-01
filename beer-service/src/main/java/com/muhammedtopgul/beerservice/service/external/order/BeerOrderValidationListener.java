package com.muhammedtopgul.beerservice.service.external.order;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.event.ValidateOrderRequestEvent;
import com.muhammedtopgul.application.common.event.ValidateOrderResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 16:56
 */

@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {

    private final BeerOrderValidator validator;
    private final JmsTemplate jmSTemplate;

    @JmsListener(destination = JmsConstants.VALIDATE_ORDER_REQUEST_QUEUE)
    public void listen(ValidateOrderRequestEvent event) {
        jmSTemplate.convertAndSend(
                JmsConstants.VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResultEvent.builder()
                        .orderId(event.getBeerOrderDto().getId())
                        .isValid(validator.validateOrder(event.getBeerOrderDto()))
                        .build());
    }
}
