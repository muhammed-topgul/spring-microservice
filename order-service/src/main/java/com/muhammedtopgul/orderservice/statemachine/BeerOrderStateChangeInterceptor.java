package com.muhammedtopgul.orderservice.statemachine;

import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.service.BeerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 15:16
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderService beerOrderService;

    @Override
    @Transactional
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state, Message<BeerOrderEventEnum> message, Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> rootStateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(StateMachineConstants.ORDER_ID_HEADER, "")))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());
                    BeerOrderEntity beerOrderEntity = beerOrderService.findById(UUID.fromString(orderId));
                    beerOrderEntity.setOrderStatus(state.getId());
                    beerOrderService.saveAndFlush(beerOrderEntity);
                });
    }
}
