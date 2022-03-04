package com.muhammedtopgul.inventoryservice.service.listener;

import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.application.common.event.DeallocateOrderRequestEvent;
import com.muhammedtopgul.inventoryservice.service.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author muhammed-topgul
 * @since 04.03.2022 15:45
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class DeallocationListener {

    private final AllocationService allocationService;

    @JmsListener(destination = JmsQueues.DEALLOCATE_ORDER_REQUEST_QUEUE)
    public void listen(DeallocateOrderRequestEvent event) {
        allocationService.deallocateOrder(event.getBeerOrderDto());
    }
}
