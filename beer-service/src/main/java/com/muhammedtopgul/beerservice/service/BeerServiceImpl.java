package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.application.common.exception.ApplicationException;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.beerservice.enumeration.BeerStyle;
import com.muhammedtopgul.beerservice.mapper.BeerMapper;
import com.muhammedtopgul.beerservice.pageable.BeerPagedList;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:30
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList findAll(String beerName, BeerStyle beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        log.debug("BeerService.findAll() called...");

        BeerPagedList beerPagedList;
        Page<BeerEntity> beerPage;

        if (!ObjectUtils.isEmpty(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle.name(), pageRequest);
        } else if (!ObjectUtils.isEmpty(beerName)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!ObjectUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle.name(), pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        beerPagedList = new BeerPagedList(
                beerPage.getContent()
                        .stream()
                        .map(beerEntity -> this.toDtoIsShowInventory(beerEntity, showInventoryOnHand))
                        .collect(Collectors.toList()),
                PageRequest.of(
                        beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()
                ),
                beerPage.getTotalElements()
        );

        return beerPagedList;
    }

    private BeerDto toDtoIsShowInventory(BeerEntity beerEntity, Boolean showInventoryOnHand) {
        if (showInventoryOnHand == null || !showInventoryOnHand)
            return beerMapper.toDto(beerEntity);
        else
            return beerMapper.toDtoWithInventoryOnHand(beerEntity);
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto findById(UUID beerId, Boolean showInventoryOnHand) {
        log.debug("BeerService.findById() called...");
        return this.toDtoIsShowInventory(beerRepository.findById(beerId)
                .orElseThrow(() -> new ApplicationException(String.format("Beer Not Found: %s", beerId), BeerEntity.class)), showInventoryOnHand);
    }

    @Override
    public BeerDto save(BeerDto beerDto) {
        return beerMapper.toDto(beerRepository.save(beerMapper.toEntity(beerDto)));
    }

    @Override
    public BeerDto update(UUID beerId, BeerDto beerDto) {
        BeerEntity beerEntity = beerRepository.findById(beerId).orElseThrow(() -> new ApplicationException(String.format("Beer Not Found: %s", beerId), BeerEntity.class));

        beerEntity.setBeerName(beerDto.getBeerName());
        beerEntity.setBeerStyle(beerDto.getBeerStyle());
        beerEntity.setPrice(beerDto.getPrice());
        beerEntity.setUpc(beerDto.getUpc());

        return beerMapper.toDto(beerRepository.save(beerEntity));
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public BeerDto findByUpc(String upc) {
        log.debug("BeerService.findByUpc() called...");
        return beerMapper.toDto(beerRepository.findByUpc(upc));
    }
}
