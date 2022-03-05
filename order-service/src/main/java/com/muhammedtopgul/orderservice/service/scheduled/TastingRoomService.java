package com.muhammedtopgul.orderservice.service.scheduled;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.application.common.dto.BeerOrderLineDto;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.initial.BeerOrderLoader;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import com.muhammedtopgul.orderservice.service.BeerOrderService;
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
    private static final List<String> beerUpcs = new ArrayList<>(3);

    private final Random random = new Random();

    static {
        beerUpcs.add(BeerOrderLoader.BEER_1_UPC);
        beerUpcs.add(BeerOrderLoader.BEER_2_UPC);
        beerUpcs.add(BeerOrderLoader.BEER_3_UPC);
    }

    @Transactional
    @Scheduled(fixedRate = 20000) //run every 2 seconds
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
                .orderQuantity(random.nextInt(6))
                .build();

        List<BeerOrderLineDto> beerOrderLineSet = new ArrayList<>();
        beerOrderLineSet.add(beerOrderLine);

        BeerOrderDto beerOrder = BeerOrderDto.builder()
                .customerId(customerEntity.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineSet)
                .build();

        beerOrderService.placeOrder(customerEntity.getId(), beerOrder);

    }

    private String getRandomBeerUpc() {
        return beerUpcs.get(random.nextInt(beerUpcs.size()));
    }
}
