package com.muhammedtopgul.inventoryservice.mapper;

import com.muhammedtopgul.inventoryservice.dto.InventoryDto;
import com.muhammedtopgul.inventoryservice.entity.InventoryEntity;
import org.mapstruct.Mapper;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:59
 */

@Mapper(uses = {DateMapper.class})
public interface InventoryMapper {

    InventoryEntity toEntity(InventoryDto dto);

    InventoryDto toDto(InventoryEntity entity);
}