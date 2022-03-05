package com.muhammedtopgul.application.common.pageable;

import com.muhammedtopgul.application.common.dto.CustomerDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 22:20
 */

public class CustomerPagedList extends PageImpl<CustomerDto> implements Serializable {
    public CustomerPagedList(List<CustomerDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CustomerPagedList(List<CustomerDto> content) {
        super(content);
    }
}
