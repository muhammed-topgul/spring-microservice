package com.muhammedtopgul.orderservice.mapper;

import com.muhammedtopgul.orderservice.dto.BeerOrderDto;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:10
 */

@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    BeerOrderDto toDto(BeerOrderEntity entity);

    BeerOrderEntity toEntity(BeerOrderDto dto);
}