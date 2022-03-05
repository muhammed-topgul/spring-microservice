package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.pageable.CustomerPagedList;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.mapper.CustomerMapper;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 23:29
 */

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerPagedList findAll(Pageable pageable) {
        Page<CustomerEntity> customerPage = customerRepository.findAll(pageable);

        return new CustomerPagedList(customerPage
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList()),
                PageRequest.of(
                        customerPage.getPageable().getPageNumber(),
                        customerPage.getPageable().getPageSize()),
                customerPage.getTotalElements());
    }
}
