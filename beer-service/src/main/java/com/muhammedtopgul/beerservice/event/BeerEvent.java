package com.muhammedtopgul.beerservice.event;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:37
 */

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 1448455814255609055L;

    private final BeerDto beerDto;
}
