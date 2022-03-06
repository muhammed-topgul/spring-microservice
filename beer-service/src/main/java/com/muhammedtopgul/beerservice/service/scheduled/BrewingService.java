package com.muhammedtopgul.beerservice.service.scheduled;

import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.application.common.event.BrewBeerEvent;
import com.muhammedtopgul.beerservice.mapper.BeerMapper;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import com.muhammedtopgul.beerservice.service.external.InventoryService;
import com.muhammedtopgul.application.common.constant.jms.JmsQueues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:43
 */

@Service
@Slf4j
@RequiredArgsConstructor
class BrewingService {

    private final BeerRepository beerRepository;
    private final InventoryService inventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void checkForLowInventory() {
        List<BeerEntity> beerEntities = beerRepository.findAll();

        beerEntities.forEach(beerEntity -> {
            Integer inventoryQuantityOnHand = inventoryService.getOnHandInventory(beerEntity.getId());

            log.debug("Min On Hand is : " + beerEntity.getMinOnHand());
            log.debug("Inventory is : " + beerEntity.getMinOnHand());

            if (beerEntity.getMinOnHand() >= inventoryQuantityOnHand) {
                jmsTemplate.convertAndSend(JmsQueues.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.toDto(beerEntity)));
            }
        });
    }
}
