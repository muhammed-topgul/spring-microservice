package com.muhammedtopgul.orderservice.service;

import com.muhammedtopgul.application.common.dto.BeerOrderDto;
import com.muhammedtopgul.application.common.enumeration.BeerOrderStatusEnum;
import com.muhammedtopgul.application.common.exception.ApplicationException;
import com.muhammedtopgul.orderservice.entity.BeerOrderEntity;
import com.muhammedtopgul.orderservice.entity.CustomerEntity;
import com.muhammedtopgul.orderservice.mapper.BeerOrderMapper;
import com.muhammedtopgul.orderservice.pageable.BeerOrderPagedList;
import com.muhammedtopgul.orderservice.repository.BeerOrderRepository;
import com.muhammedtopgul.orderservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 13:19
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Page<BeerOrderEntity> beerOrderPage = beerOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

            return new BeerOrderPagedList(beerOrderPage
                    .stream()
                    .map(beerOrderMapper::toDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    beerOrderPage.getPageable().getPageNumber(),
                    beerOrderPage.getPageable().getPageSize()),
                    beerOrderPage.getTotalElements());
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            BeerOrderEntity beerOrder = beerOrderMapper.toEntity(beerOrderDto);
            beerOrder.setId(null); // should not be set by outside client
            beerOrder.setCustomer(customerOptional.get());
            beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

            if (beerOrder.getBeerOrderLines() != null) {
                beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));
            }

            BeerOrderEntity savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

            log.debug("Saved Beer Order: " + beerOrder.getId());

            return beerOrderMapper.toDto(savedBeerOrder);
        }

        throw new ApplicationException(String.format("Customer Not Found: %s", customerId), CustomerEntity.class);
    }

    @Override
    public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
        return beerOrderMapper.toDto(getOrder(customerId, orderId));
    }

    @Override
    public BeerOrderEntity findById(UUID uuid) {
        Optional<BeerOrderEntity> beerOrderOptional = beerOrderRepository.findById(uuid);

        if (beerOrderOptional.isPresent()) {
            return beerOrderOptional.get();
        }
        throw new ApplicationException(String.format("Beer Order Not Found: %s", uuid), BeerOrderEntity.class);
    }

    @Override
    public BeerOrderEntity saveAndFlush(BeerOrderEntity beerOrderEntity) {
        return beerOrderRepository.saveAndFlush(beerOrderEntity);
    }

    @Override
    public BeerOrderEntity save(BeerOrderEntity beerOrderEntity) {
        return beerOrderRepository.save(beerOrderEntity);
    }

    @Override
    public void pickupOrder(UUID customerId, UUID orderId) {
        BeerOrderEntity beerOrder = getOrder(customerId, orderId);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.PICKED_UP);

        beerOrderRepository.save(beerOrder);
    }

    private BeerOrderEntity getOrder(UUID customerId, UUID orderId) {
        Optional<CustomerEntity> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            BeerOrderEntity beerOrder = this.findById(orderId);
            // fall to exception if customer id's do not match - order not for customer
            if (beerOrder.getCustomer().getId().equals(customerId)) {
                return beerOrder;
            }
        }
        throw new ApplicationException(String.format("Customer Not Found: %s", customerId), CustomerEntity.class);
    }
}
