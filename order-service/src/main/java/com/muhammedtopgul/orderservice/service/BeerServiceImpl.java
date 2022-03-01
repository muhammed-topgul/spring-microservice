package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.dto.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 24.02.2022 13:55
 */

@Service
@ConfigurationProperties(prefix = "external.service", ignoreUnknownFields = false)
@Setter
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {

    private final RestTemplate restTemplate;

    private String beerServiceHost;
    private String beerServicePath;
    private String beerServiceByUpcPath;

    @Override
    public Optional<BeerDto> findById(UUID uuid) {
        return Optional.of(
                restTemplate.getForObject(this.getBeerServiceUrl(true) + uuid.toString(), BeerDto.class)
        );
    }

    @Override
    public Optional<BeerDto> findByUpc(String upc) {
        return Optional.of(
                restTemplate.getForObject(this.getBeerServiceUrl(false) + upc, BeerDto.class)
        );
    }

    private String getBeerServiceUrl(boolean withUuid) {
        if (withUuid)
            return this.beerServiceHost + beerServicePath;
        else
            return this.beerServiceHost + beerServiceByUpcPath;
    }
}
