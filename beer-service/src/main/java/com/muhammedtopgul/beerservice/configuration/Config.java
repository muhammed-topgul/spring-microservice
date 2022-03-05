package com.muhammedtopgul.beerservice.configuration;

import com.muhammedtopgul.application.config.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 16:21
 */

@Configuration
@Import(value = {
        BeanConfig.class,
        CacheConfig.class,
        EurekaLocalDiscoveryConfig.class,
        JmsConfig.class,
        SchedulingConfig.class})
public class Config {
}
