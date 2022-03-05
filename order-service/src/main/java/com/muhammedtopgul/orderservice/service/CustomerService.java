package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.pageable.CustomerPagedList;
import org.springframework.data.domain.Pageable;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 23:28
 */

public interface CustomerService {

    CustomerPagedList findAll(Pageable pageable);
}
