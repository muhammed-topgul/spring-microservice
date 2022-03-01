package com.muhammedtopgul.orderservice.service.statemachine;

import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.statemachine.BeerOrderStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 15:01
 */

@Service
@RequiredArgsConstructor
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;

    @Override
    @Transactional
    public BeerOrderEntity newBeerOrderEntity(BeerOrderEntity beerOrderEntity) {
        beerOrderEntity.setId(null);
        beerOrderEntity.setOrderStatus(BeerOrderStatusEnum.NEW);
        beerOrderEntity = beerOrderRepository.save(beerOrderEntity);
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.VALIDATE_ORDER);
        return beerOrderEntity;
    }

    @Override
    public void processValidationResult(UUID beerOrderId, Boolean isValid) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findOneById(beerOrderId);
        sendBeerOrderEvent(beerOrderEntity, isValid ? BeerOrderEventEnum.VALIDATION_PASSED : BeerOrderEventEnum.VALIDATION_FAILED);
    }

    private void sendBeerOrderEvent(BeerOrderEntity beerOrderEntity, BeerOrderEventEnum eventEnum) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = this.build(beerOrderEntity);
        Message<BeerOrderEventEnum> message = MessageBuilder.withPayload(eventEnum)
                .setHeader(StateMachineConstants.ORDER_ID_HEADER, beerOrderEntity.getId().toString())
                .build();

        stateMachine.sendEvent(message);
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrderEntity beerOrderEntity) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = stateMachineFactory.getStateMachine(beerOrderEntity.getId());

        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(accessor -> {
                    accessor.addStateMachineInterceptor(beerOrderStateChangeInterceptor);
                    accessor.resetStateMachine(new DefaultStateMachineContext<>(beerOrderEntity.getOrderStatus(), null, null, null));
                });

        stateMachine.start();

        return stateMachine;
    }
}
