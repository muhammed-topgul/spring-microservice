package com.muhammedtopgul.orderservice.mapper;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.application.common.dto.BeerOrderLineDto;
import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import com.muhammedtopgul.orderservice.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

/**
 * @author muhammed-topgul
 * @since 24.02.2022 14:04
 */

public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    @Autowired
    private BeerService beerService;

    @Autowired
    @Qualifier("delegate")
    private BeerOrderLineMapper beerOrderLineMapper;

    @Override
    public BeerOrderLineDto toDto(BeerOrderLineEntity beerOrderLineEntity) {
        BeerOrderLineDto orderLineDto = beerOrderLineMapper.toDto(beerOrderLineEntity);
        Optional<BeerDto> beerDtoOptional = beerService.findByUpc(beerOrderLineEntity.getUpc());

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerStyle());
            orderLineDto.setPrice(beerDto.getPrice());
            orderLineDto.setBeerId(beerDto.getId());
        });

        return orderLineDto;
    }
}
