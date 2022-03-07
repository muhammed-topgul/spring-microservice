package com.muhammedtopgul.beerservice.service.external.inventory.feign.failover;

import com.muhammedtopgul.application.common.dto.InventoryDto;
import com.muhammedtopgul.beerservice.service.external.inventory.feign.InventoryFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 07.03.2022 12:32
 */

@Component
@RequiredArgsConstructor
public class InventoryFailoverFeignClientService implements InventoryFeignClient {

    private final InventoryFailoverFeignClient failoverFeignClient;

    @Override
    public ResponseEntity<List<InventoryDto>> getOnHandInventory(UUID beerId) {
        return failoverFeignClient.getOnHandInventory();
    }
}
