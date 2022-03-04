package com.muhammedtopgul.inventoryservice.service.listener;

import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.application.common.event.AllocateOrderRequestEvent;
import com.muhammedtopgul.application.common.event.AllocateOrderResultEvent;
import com.muhammedtopgul.inventoryservice.service.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 15:19
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocationListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsQueues.ALLOCATE_ORDER_REQUEST_QUEUE)
    public void listen(AllocateOrderRequestEvent event) {
        AllocateOrderResultEvent allocateOrderResultEvent = new AllocateOrderResultEvent();
        allocateOrderResultEvent.setBeerOrderDto(event.getBeerOrderDto());

        try {
            Boolean allocationResult = allocationService.allocateOrder(event.getBeerOrderDto());
            allocateOrderResultEvent.setPendingInventory(!allocationResult);
        } catch (Exception e) {
            log.error(">>> Allocation failed for Order Id: " + event.getBeerOrderDto().getId());
            allocateOrderResultEvent.setAllocationError(true);
        }

        jmsTemplate.convertAndSend(JmsQueues.ALLOCATE_ORDER_RESPONSE_QUEUE, allocateOrderResultEvent);
    }
}
