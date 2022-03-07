package com.muhammedtopgul.inventoryfailover.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author muhammed-topgul
 * @since 07.03.2022 10:25
 */

@Profile("eureka-local-discovery")
@Configuration
@EnableDiscoveryClient
public class Config {
}
