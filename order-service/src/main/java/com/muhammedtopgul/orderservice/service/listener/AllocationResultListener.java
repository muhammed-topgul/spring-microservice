package com.muhammedtopgul.orderservice.service.listener;

import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.application.common.event.AllocateOrderResultEvent;
import com.muhammedtopgul.orderservice.service.statemachine.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 15:41
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsQueues.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listen(AllocateOrderResultEvent event) {
        if (!event.isAllocationError() && !event.isPendingInventory()) {
            // allocated normally
            beerOrderManager.beerOrderAllocationPassed(event.getBeerOrderDto());
        } else if (!event.isAllocationError() && event.isPendingInventory()) {
            // pending inventory
            beerOrderManager.beerOrderAllocationPendingInventory(event.getBeerOrderDto());
        } else if (event.isAllocationError()) {
            // allocation error
            beerOrderManager.beerOrderAllocationFailed(event.getBeerOrderDto());
        }
    }
}
