package com.muhammedtopgul.orderservice.statemachine.action;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.event.ValidateOrderRequestEvent;
import com.muhammedtopgul.application.common.exception.ApplicationException;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.mapper.BeerOrderMapper;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
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
 * @since 01.03.2022 15:55
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(StateMachineConstants.ORDER_ID_HEADER);
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(UUID.fromString(beerOrderId))
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderId), BeerOrderEntity.class));

        jmsTemplate.convertAndSend(
                JmsConstants.VALIDATE_ORDER_REQUEST_QUEUE,
                ValidateOrderRequestEvent.builder()
                        .beerOrderDto(beerOrderMapper.toDto(beerOrderEntity))
                        .build());

        log.debug(">>> Sent Validation request to queue for order id: " + beerOrderId);
    }
}
