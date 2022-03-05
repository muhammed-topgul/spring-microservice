package com.muhammedtopgul.inventoryservice.configuration;

import com.muhammedtopgul.application.config.EurekaLocalDiscoveryConfig;
import com.muhammedtopgul.application.config.JmsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 16:24
 */

@Configuration
@Import(value = {
        EurekaLocalDiscoveryConfig.class,
        JmsConfig.class})
public class Config {
}
