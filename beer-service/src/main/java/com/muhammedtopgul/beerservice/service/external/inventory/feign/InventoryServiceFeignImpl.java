package com.muhammedtopgul.beerservice.service.external.inventory.feign;

import com.muhammedtopgul.application.common.dto.InventoryDto;
import com.muhammedtopgul.beerservice.service.external.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 06.03.2022 22:01
 */

@Service
@Profile("local-discovery")
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceFeignImpl implements InventoryService {

    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling inventory service feign impl...");
        ResponseEntity<List<InventoryDto>> responseEntity = inventoryFeignClient.getOnHandInventory(beerId);

        Integer inventoryOnHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(InventoryDto::getQuantityOnHand)
                .sum();

        log.debug("BeerId: " + beerId + " On Hand is: " + inventoryOnHand);

        return inventoryOnHand;
    }
}
