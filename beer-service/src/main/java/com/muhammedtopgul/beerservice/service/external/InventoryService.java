package com.muhammedtopgul.beerservice.service.external;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 23.02.2022 15:52
 */

public interface InventoryService {

    Integer getOnHandInventory(UUID beerId);
}
