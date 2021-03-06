package com.muhammedtopgul.beerservice.mapper;

import com.muhammedtopgul.application.common.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:31
 */

@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper {

    BeerDto toDtoWithInventoryOnHand(BeerEntity entity);

    BeerDto toDto(BeerEntity entity);

    BeerEntity toEntity(BeerDto dto);
}
