package com.muhammedtopgul.orderservice.statemachine.action;

import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.event.AllocationFailureEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 04.03.2022 13:54
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(StateMachineConstants.ORDER_ID_HEADER);

        jmsTemplate.convertAndSend(
                JmsQueues.ALLOCATE_FAILURE_QUEUE,
                AllocationFailureEvent.builder().orderId(UUID.fromString(beerOrderId)).build());

        log.debug(">>> Sent Allocation Failure Message to queue for order id: " + beerOrderId);
    }
}