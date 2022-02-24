package com.muhammedtopgul.orderservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author muhammed-topgul
 * @since 24.02.2022 14:13
 */

@Configuration
public class BeanConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
