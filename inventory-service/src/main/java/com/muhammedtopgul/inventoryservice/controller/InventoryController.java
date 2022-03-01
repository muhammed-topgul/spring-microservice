package com.muhammedtopgul.inventoryservice.controller;

import com.muhammedtopgul.application.common.dto.InventoryDto;
import com.muhammedtopgul.inventoryservice.mapper.InventoryMapper;
import com.muhammedtopgul.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 14:03
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @GetMapping("api/v1/beer/{beerId}/inventory")
    List<InventoryDto> listBeersById(@PathVariable UUID beerId) {
        log.debug("Finding Inventory for beerId:" + beerId);

        return inventoryRepository.findAllByBeerId(beerId)
                .stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
