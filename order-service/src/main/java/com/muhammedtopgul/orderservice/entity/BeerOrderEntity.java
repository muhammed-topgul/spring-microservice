package com.muhammedtopgul.orderservice.entity;

import com.muhammedtopgul.orderservice.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:59
 */

@Getter
@Setter
@Entity
public class BeerOrderEntity extends BaseEntity {

    public BeerOrderEntity() {
    }

    @Builder
    public BeerOrderEntity(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String customerRef, CustomerEntity customer,
                           Set<BeerOrderLineEntity> beerOrderLines, OrderStatus orderStatus,
                           String orderStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.customerRef = customerRef;
        this.customer = customer;
        this.beerOrderLines = beerOrderLines;
        this.orderStatus = orderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }

    private String customerRef;

    @ManyToOne
    private CustomerEntity customer;

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private Set<BeerOrderLineEntity> beerOrderLines;

    private OrderStatus orderStatus = OrderStatus.NEW;
    private String orderStatusCallbackUrl;
}
