package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.orderservice.dto.BeerOrderDto;
import com.muhammedtopgul.orderservice.dto.BeerOrderLineDto;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.initial.BeerOrderLoader;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:22
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TastingRoomService {

    private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerOrderRepository beerOrderRepository;
    private final List<String> beerUpcs = new ArrayList<>(3);

    {
        beerUpcs.add(BeerOrderLoader.BEER_1_UPC);
        beerUpcs.add(BeerOrderLoader.BEER_2_UPC);
        beerUpcs.add(BeerOrderLoader.BEER_3_UPC);
    }

    @Transactional
    @Scheduled(fixedRate = 2000) //run every 2 seconds
    public void placeTastingRoomOrder() {

        List<CustomerEntity> customerList = customerRepository.findAllByCustomerNameLike(BeerOrderLoader.TASTING_ROOM);

        if (customerList.size() == 1) { //should be just one
            doPlaceOrder(customerList.get(0));
        } else {
            log.error("Too many or too few tasting room customers found");
        }
    }

    private void doPlaceOrder(CustomerEntity customerEntity) {
        String beerToOrder = getRandomBeerUpc();

        BeerOrderLineDto beerOrderLine = BeerOrderLineDto.builder()
                .upc(beerToOrder)
                .orderQuantity(new Random().nextInt(6)) //todo externalize value to property
                .build();

        List<BeerOrderLineDto> beerOrderLineSet = new ArrayList<>();
        beerOrderLineSet.add(beerOrderLine);

        BeerOrderDto beerOrder = BeerOrderDto.builder()
                .customerId(customerEntity.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineSet)
                .build();

        BeerOrderDto savedOrder = beerOrderService.placeOrder(customerEntity.getId(), beerOrder);

    }

    private String getRandomBeerUpc() {
        return beerUpcs.get(new Random().nextInt(beerUpcs.size() - 0));
    }
}
