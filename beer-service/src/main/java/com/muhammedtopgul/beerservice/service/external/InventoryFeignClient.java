package com.muhammedtopgul.beerservice.service.external;

import com.muhammedtopgul.application.common.dto.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 06.03.2022 21:50
 */

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

    @GetMapping(value = "${external.service.inventory-service-path}")
    ResponseEntity<List<InventoryDto>> getOnHandInventory(@PathVariable UUID beerId);
}
