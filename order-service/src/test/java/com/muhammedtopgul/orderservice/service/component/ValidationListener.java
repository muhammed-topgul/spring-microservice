package com.muhammedtopgul.orderservice.service.component;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.constant.test.CustomerRefConstants;
import com.muhammedtopgul.application.common.event.ValidateOrderRequestEvent;
import com.muhammedtopgul.application.common.event.ValidateOrderResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 03.03.2022 13:39
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConstants.VALIDATE_ORDER_REQUEST_QUEUE)
    public void listen(Message message) {
        ValidateOrderRequestEvent event = (ValidateOrderRequestEvent) message.getPayload();

        boolean isValid = event.getBeerOrderDto().getCustomerRef() == null ||
                !event.getBeerOrderDto().getCustomerRef().equals(CustomerRefConstants.FAIL_VALIDATION);

        ValidateOrderResultEvent resultEvent = new ValidateOrderResultEvent();
        resultEvent.setOrderId(event.getBeerOrderDto().getId());
        resultEvent.setIsValid(isValid);

        jmsTemplate.convertAndSend(JmsConstants.VALIDATE_ORDER_RESPONSE_QUEUE, resultEvent);
    }
}
