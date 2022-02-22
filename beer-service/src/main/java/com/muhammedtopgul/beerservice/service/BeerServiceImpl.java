package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.beerservice.exception.NotFoundException;
import com.muhammedtopgul.beerservice.mapper.BeerMapper;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:30
 */

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto findById(UUID beerId) {
        return beerMapper.toDto(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
    }

    @Override
    public BeerDto save(BeerDto beerDto) {
        return beerMapper.toDto(beerRepository.save(beerMapper.toEntity(beerDto)));
    }

    @Override
    public BeerDto update(UUID beerId, BeerDto beerDto) {
        BeerEntity beerEntity = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beerEntity.setName(beerDto.getName());
        beerEntity.setStyle(beerDto.getStyle().name());
        beerEntity.setPrice(beerDto.getPrice());
        beerEntity.setUpc(beerDto.getUpc());

        return beerMapper.toDto(beerRepository.save(beerEntity));
    }
}
