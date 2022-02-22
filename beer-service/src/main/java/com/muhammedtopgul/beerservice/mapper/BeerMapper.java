package com.muhammedtopgul.beerservice.mapper;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import org.mapstruct.Mapper;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:31
 */

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto toDto(BeerEntity entity);

    BeerEntity toEntity(BeerDto dto);
}
