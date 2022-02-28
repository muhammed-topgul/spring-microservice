package com.muhammedtopgul.application.common.event;

import com.muhammedtopgul.application.common.dto.BeerDto;
import lombok.*;

import java.io.Serializable;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:37
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 1448455814255609055L;

    private BeerDto beerDto;
}
