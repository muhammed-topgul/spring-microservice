package com.muhammedtopgul.beerservice.service.external.inventory.feign.failover;

import com.muhammedtopgul.application.common.dto.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author muhammed-topgul
 * @since 07.03.2022 12:29
 */

@FeignClient(name = "inventory-failover")
public interface InventoryFailoverFeignClient {

    @GetMapping(value = "/inventory-failover")
    ResponseEntity<List<InventoryDto>> getOnHandInventory();
}
