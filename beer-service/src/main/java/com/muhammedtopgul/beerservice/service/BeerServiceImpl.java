package com.muhammedtopgul.beerservice.service;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.beerservice.enumeration.BeerStyle;
import com.muhammedtopgul.beerservice.exception.NotFoundException;
import com.muhammedtopgul.beerservice.mapper.BeerMapper;
import com.muhammedtopgul.beerservice.pageable.BeerPagedList;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

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
    public BeerPagedList findAll(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        BeerPagedList beerPagedList;
        Page<BeerEntity> beerPage;

        if (!ObjectUtils.isEmpty(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle.name(), pageRequest);
        } else if (!ObjectUtils.isEmpty(beerName)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!ObjectUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle.name(), pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        beerPagedList = new BeerPagedList(
                beerPage.getContent()
                        .stream()
                        .map(beerMapper::toDto)
                        .collect(Collectors.toList()),
                PageRequest.of(
                        beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()
                ),
                beerPage.getTotalElements()
        );

        return beerPagedList;
    }

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

        beerEntity.setBeerName(beerDto.getBeerName());
        beerEntity.setBeerStyle(beerDto.getBeerStyle());
        beerEntity.setPrice(beerDto.getPrice());
        beerEntity.setUpc(beerDto.getUpc());

        return beerMapper.toDto(beerRepository.save(beerEntity));
    }
}
