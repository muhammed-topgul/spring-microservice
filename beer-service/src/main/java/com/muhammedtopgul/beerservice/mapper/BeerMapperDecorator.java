package com.muhammedtopgul.beerservice.mapper;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.beerservice.service.external.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author muhammed-topgul
 * @since 23.02.2022 16:09
 */

// TODO use @AfterMapping in BeerMapper interface
public abstract class BeerMapperDecorator implements BeerMapper {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BeerMapper beerMapper;

    @Override
    public BeerDto toDtoWithInventoryOnHand(BeerEntity entity) {
        BeerDto beerDto = beerMapper.toDto(entity);
        beerDto.setQuantityOnHand(inventoryService.getOnHandInventory(entity.getId()));
        return beerDto;
    }

    @Override
    public BeerDto toDto(BeerEntity entity) {
        return beerMapper.toDto(entity);
    }

    @Override
    public BeerEntity toEntity(BeerDto dto) {
        return beerMapper.toEntity(dto);
    }
}