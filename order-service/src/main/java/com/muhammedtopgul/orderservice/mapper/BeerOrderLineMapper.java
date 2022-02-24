package com.muhammedtopgul.orderservice.mapper;

import com.muhammedtopgul.orderservice.dto.BeerOrderLineDto;
import com.muhammedtopgul.orderservice.entity.BeerOrderLineEntity;
import org.mapstruct.Mapper;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:11
 */

@Mapper(uses = {DateMapper.class})
public interface BeerOrderLineMapper {

    BeerOrderLineDto toDto(BeerOrderLineEntity entity);

    BeerOrderLineEntity toEntity(BeerOrderLineDto dto);
}
