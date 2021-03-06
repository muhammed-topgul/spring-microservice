package com.muhammedtopgul.orderservice.service.statemachine;

import com.muhammedtopgul.application.common.constant.statemachine.StateMachineConstants;
import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.application.common.enumeration.BeerOrderEventEnum;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.exception.ApplicationException;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.statemachine.BeerOrderStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 15:01
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public BeerOrderEntity newBeerOrderEntity(BeerOrderEntity beerOrderEntity) {
        beerOrderEntity.setId(null);
        beerOrderEntity.setOrderStatus(BeerOrderStatusEnum.NEW);
        beerOrderEntity = beerOrderRepository.saveAndFlush(beerOrderEntity);
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.VALIDATE_ORDER);
        return beerOrderEntity;
    }

    @Override
    @Transactional
    public void processValidationResult(UUID beerOrderId, boolean isValid) {
        entityManager.flush();
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(beerOrderId)
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderId), BeerOrderEntity.class));

        if (isValid) {
            sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.VALIDATION_PASSED);
            beerOrderEntity = beerOrderRepository.findById(beerOrderId)
                    .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderId), BeerOrderEntity.class));
            sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.ALLOCATE_ORDER);
        } else {
            sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.VALIDATION_FAILED);
        }
    }

    @Override
    public void beerOrderAllocationPassed(BeerOrderDto beerOrderDto) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(beerOrderDto.getId())
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderDto.getId()), BeerOrderEntity.class));
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.ALLOCATION_SUCCESS);
        this.updateAllocatedQuantity(beerOrderDto);
    }

    @Override
    public void beerOrderAllocationPendingInventory(BeerOrderDto beerOrderDto) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(beerOrderDto.getId())
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderDto.getId()), BeerOrderEntity.class));
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.ALLOCATION_NO_INVENTORY);
        this.updateAllocatedQuantity(beerOrderDto);
    }

    @Override
    public void beerOrderAllocationFailed(BeerOrderDto beerOrderDto) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(beerOrderDto.getId())
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderDto.getId()), BeerOrderEntity.class));
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.ALLOCATION_FAILED);
    }

    @Override
    public void beerOrderPickedUp(UUID id) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", id), BeerOrderEntity.class));
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.BEER_ORDER_PICKED_UP);
    }

    @Override
    public void cancelOrder(UUID id) {
        BeerOrderEntity beerOrderEntity = beerOrderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", id), BeerOrderEntity.class));
        sendBeerOrderEvent(beerOrderEntity, BeerOrderEventEnum.CANCEL_ORDER);
    }

    private void updateAllocatedQuantity(BeerOrderDto beerOrderDto) {
        BeerOrderEntity allocatedOrder = beerOrderRepository.findById(beerOrderDto.getId())
                .orElseThrow(() -> new ApplicationException(String.format("Beer Order Not Found: %s", beerOrderDto.getId()), BeerOrderEntity.class));

        allocatedOrder.getBeerOrderLines().forEach(beerOrderLineEntity -> beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            if (beerOrderLineEntity.getId().equals(beerOrderLineDto.getId())) {
                beerOrderLineEntity.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated());
            }
        }));

        beerOrderRepository.saveAndFlush(allocatedOrder);
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
