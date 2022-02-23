package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.beerservice.dto.InventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 23.02.2022 15:56
 */

@Service
@ConfigurationProperties(prefix = "external.service", ignoreUnknownFields = false)
@Setter
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final RestTemplate restTemplate;

    private String inventoryServiceHost;
    private String inventoryServicePath;

    @Override
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling inventory service...");

        ResponseEntity<List<InventoryDto>> inventories = restTemplate.exchange(
                this.getInventoryServiceUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                beerId);

        return Objects.requireNonNull(inventories.getBody())
                .stream()
                .mapToInt(InventoryDto::getQuantityOnHand)
                .sum();
    }

    private String getInventoryServiceUrl() {
        return this.inventoryServiceHost + inventoryServicePath;
    }
}
