package com.muhammedtopgul.beerservice.service.external.order;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author muhammed-topgul
 * @since 01.03.2022 16:58
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerOrderValidator {

    private final BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrderDto) {
        AtomicInteger beersNotFound = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(orderLine -> {
            if (beerRepository.findByUpc(orderLine.getUpc()) == null) {
                beersNotFound.incrementAndGet();
            }
        });

        return beersNotFound.get() == 0;
    }
}
