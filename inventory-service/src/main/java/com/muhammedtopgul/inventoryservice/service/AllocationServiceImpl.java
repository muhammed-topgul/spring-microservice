package com.muhammedtopgul.inventoryservice.service;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.application.common.dto.BeerOrderLineDto;
import com.muhammedtopgul.inventoryservice.entity.InventoryEntity;
import com.muhammedtopgul.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author muhammed-topgul
 * @since 02.03.2022 14:50
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AllocationServiceImpl implements AllocationService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        log.debug(">>> Allocating OrderId: " + beerOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLine -> {
            if (((beerOrderLine.getOrderQuantity() != null ? beerOrderLine.getOrderQuantity() : 0)
                    - (beerOrderLine.getQuantityAllocated() != null ? beerOrderLine.getQuantityAllocated() : 0)) > 0) {
                this.allocateBeerOrderLine(beerOrderLine);
            }

            totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + (beerOrderLine.getQuantityAllocated() != null ? beerOrderLine.getOrderQuantity() : 0));
        });

        log.debug(">>> Total Ordered: " + totalAllocated.get() + ", Total Allocated: " + totalAllocated.get());
        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BeerOrderDto beerOrderDto) {
        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            InventoryEntity inventoryEntity = InventoryEntity.builder()
                    .beerId(beerOrderLineDto.getBeerId())
                    .upc(beerOrderLineDto.getUpc())
                    .quantityOnHand(beerOrderLineDto.getQuantityAllocated())
                    .build();

            inventoryEntity = inventoryRepository.save(inventoryEntity);

            log.debug(">>> Sent Inventory for beer up: " + inventoryEntity.getUpc() + " inventory id: " + inventoryEntity.getId());
        });
    }

    private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {
        List<InventoryEntity> inventoryEntityList = inventoryRepository.findAllByUpc(beerOrderLine.getUpc());

        inventoryEntityList.forEach(inventoryEntity -> {
            int inventory = (inventoryEntity.getQuantityOnHand() == null) ? 0 : inventoryEntity.getQuantityOnHand();
            int orderQuantity = (beerOrderLine.getOrderQuantity() == 0) ? 0 : beerOrderLine.getOrderQuantity();
            int allocatedQuantity = (beerOrderLine.getQuantityAllocated() == null) ? 0 : beerOrderLine.getQuantityAllocated();
            int quantityToAllacate = orderQuantity - allocatedQuantity;

            if (inventory >= quantityToAllacate) { // full allocation
                inventory = inventory - quantityToAllacate;
                beerOrderLine.setQuantityAllocated(orderQuantity);
                inventoryEntity.setQuantityOnHand(inventory);

                inventoryRepository.save(inventoryEntity);
            } else if (inventory > 0) { // partial allocation
                beerOrderLine.setQuantityAllocated(allocatedQuantity + inventory);
                inventoryEntity.setQuantityOnHand(0);
            }

            if (inventoryEntity.getQuantityOnHand().equals(0)) {
                inventoryRepository.delete(inventoryEntity);
            }
        });
    }
}
