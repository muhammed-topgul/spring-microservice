package com.muhammedtopgul.beerservice.service.external.brewing;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.application.common.event.BrewBeerEvent;
import com.muhammedtopgul.application.common.event.NewInventoryEvent;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import com.muhammedtopgul.application.common.jms.JmsConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 21:12
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConstant.BREWING_REQUEST_QUEUE)
    @Transactional
    public void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();

        BeerEntity beerEntity = beerRepository.getById(beerDto.getId());
        beerDto.setQuantityOnHand(beerEntity.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer " + beerEntity.getMinOnHand() + ", QOH: " + beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConstant.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}
