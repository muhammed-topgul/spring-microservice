package com.muhammedtopgul.orderservice.mapper;

import com.muhammedtopgul.application.common.dto.CustomerDto;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import org.mapstruct.Mapper;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 22:22
 */

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {

    CustomerDto toDto(CustomerEntity entity);

    CustomerEntity toEntity(CustomerDto dto);
}
