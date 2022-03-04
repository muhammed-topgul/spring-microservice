package com.muhammedtopgul.orderservice.service.component;

import com.muhammedtopgul.application.common.constant.jms.JmsConstants;
import com.muhammedtopgul.application.common.constant.test.CustomerRefConstants;
import com.muhammedtopgul.application.common.event.AllocateOrderRequestEvent;
import com.muhammedtopgul.application.common.event.AllocateOrderResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 03.03.2022 15:43
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConstants.ALLOCATE_ORDER_REQUEST_QUEUE)
    public void listen(AllocateOrderRequestEvent event) {
        boolean pendingInventory = this.check(event, CustomerRefConstants.PARTIAL_ALLOCATION);
        boolean allocationError = this.check(event, CustomerRefConstants.FAIL_ALLOCATION);

        event.getBeerOrderDto().getBeerOrderLines().forEach(beerOrderLineDto -> {
            if (pendingInventory) {
                beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getOrderQuantity() - 1);
            } else {
                beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getOrderQuantity());
            }
        });

        AllocateOrderResultEvent allocateOrderResultEvent = new AllocateOrderResultEvent();
        allocateOrderResultEvent.setBeerOrderDto(event.getBeerOrderDto());
        allocateOrderResultEvent.setPendingInventory(pendingInventory);
        allocateOrderResultEvent.setAllocationError(allocationError);

        jmsTemplate.convertAndSend(JmsConstants.ALLOCATE_ORDER_RESPONSE_QUEUE, allocateOrderResultEvent);
    }

    private boolean check(AllocateOrderRequestEvent event, String ref) {
        return event.getBeerOrderDto().getCustomerRef() != null && event.getBeerOrderDto().getCustomerRef().equals(ref);
    }
}