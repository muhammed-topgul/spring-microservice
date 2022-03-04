package com.muhammedtopgul.inventoryservice.service.listener;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.application.common.event.NewInventoryEvent;
import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import com.muhammedtopgul.inventoryservice.entity.InventoryEntity;
import com.muhammedtopgul.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 15:58
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class NewEventListener {

    private final InventoryRepository inventoryRepository;

    @JmsListener(destination = JmsQueues.NEW_INVENTORY_QUEUE)
    @Transactional
    public void listen(NewInventoryEvent event) {
        BeerDto beerDto = event.getBeerDto();

        if (beerDto != null) {
            log.debug(">>> Got New Inventory: " + beerDto);

            inventoryRepository.save(
                    InventoryEntity.builder()
                            .beerId(beerDto.getId())
                            .upc(beerDto.getUpc())
                            .quantityOnHand(beerDto.getQuantityOnHand())
                            .build()
            );
        } else {
            log.debug(">>> BeerDto.class in null!!!");
        }

    }
}
