package com.muhammedtopgul.beerservice.conroller;

import com.muhammedtopgul.beerservice.dto.BeerDto;
import com.muhammedtopgul.beerservice.service.BeerService;
import lombok.RequiredArgsConstructor;
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

    private final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> findById(@PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerService.findById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> save(@RequestBody @Validated BeerDto beerDto){
        return new ResponseEntity<>(beerService.save(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> update(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto){
        return new ResponseEntity<>(beerService.update(beerId, beerDto), HttpStatus.NO_CONTENT);
    }
}
