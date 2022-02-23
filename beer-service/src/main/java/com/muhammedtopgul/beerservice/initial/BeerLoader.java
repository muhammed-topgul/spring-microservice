package com.muhammedtopgul.beerservice.initial;

import com.muhammedtopgul.beerservice.entity.BeerEntity;
import com.muhammedtopgul.beerservice.enumeration.BeerName;
import com.muhammedtopgul.beerservice.enumeration.BeerStyle;
import com.muhammedtopgul.beerservice.repository.BeerRepository;
import com.muhammedtopgul.beerservice.util.UpcGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 11:29
 */

// @Component
@RequiredArgsConstructor
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) {
        if (beerRepository.count() == 0) {
            beerRepository.save(
                    BeerEntity.builder()
                            .beerName(BeerName.BUD_LIGHT.getName())
                            .beerStyle(BeerStyle.IPA.toString())
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(UpcGenerator.generateUpc())
                            .price(new BigDecimal("12.95"))
                            .build());

            beerRepository.save(
                    BeerEntity.builder()
                            .beerName(BeerName.HARBIN.getName())
                            .beerStyle(BeerStyle.PALE_ALE.toString())
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(UpcGenerator.generateUpc())
                            .price(new BigDecimal("11.95"))
                            .build());
        }
        System.out.println("Loaded Beers: " + beerRepository.count());
    }
}
