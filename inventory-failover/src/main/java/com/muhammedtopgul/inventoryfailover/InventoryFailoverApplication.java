package com.muhammedtopgul.inventoryfailover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;

@SpringBootApplication(exclude = {ArtemisAutoConfiguration.class})
public class InventoryFailoverApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryFailoverApplication.class, args);
    }

}
