package com.muhammedtopgul.beerservice.conroller;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.enumeration.BeerStyle;
import com.muhammedtopgul.beerservice.pageable.BeerPagedList;
import com.muhammedtopgul.beerservice.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:47
 */

@RestController
@RequestMapping("/api/v1/beer")
@RequiredArgsConstructor
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<BeerPagedList> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @RequestParam(value = "beerName", required = false) String beerName,
                                                 @RequestParam(value = "beerStyle", required = false) BeerStyle beerStyle) {

        pageNumber = (pageNumber == null || pageNumber < 0) ? DEFAULT_PAGE_NUMBER : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;

        BeerPagedList pagedList = beerService.findAll(beerName, beerStyle, PageRequest.of(pageNumber, pageSize));

        return new ResponseEntity<>(pagedList, HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> findById(@PathVariable("beerId") UUID beerId) {
        return new ResponseEntity<>(beerService.findById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> save(@RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<>(beerService.save(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> update(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<>(beerService.update(beerId, beerDto), HttpStatus.NO_CONTENT);
    }
}
