package com.muhammedtopgul.orderservice.statemachine.action;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.event.AllocateOrderRequestEvent;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.mapper.BeerOrderMapper;
import com.muhammedtopgul.orderservice.service.BeerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 14:25
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderService beerOrderService;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(StateMachineConstants.ORDER_ID_HEADER);
        BeerOrderEntity beerOrderEntity = beerOrderService.findById(UUID.fromString(beerOrderId));

        AllocateOrderRequestEvent requestEvent = new AllocateOrderRequestEvent(beerOrderMapper.toDto(beerOrderEntity));
        jmsTemplate.convertAndSend(
                JmsConstants.ALLOCATE_ORDER_REQUEST_QUEUE,
                requestEvent);
        log.debug(">>> Sent Allocation Request for order id: " + beerOrderId);
    }
}
