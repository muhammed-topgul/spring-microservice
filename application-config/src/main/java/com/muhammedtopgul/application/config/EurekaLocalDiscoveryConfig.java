package com.muhammedtopgul.application.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author muhammed-topgul
 * @since 06.03.2022 01:02
 */

@Profile("local-discovery")
@Configuration
@EnableDiscoveryClient
public class EurekaLocalDiscoveryConfig {
}
