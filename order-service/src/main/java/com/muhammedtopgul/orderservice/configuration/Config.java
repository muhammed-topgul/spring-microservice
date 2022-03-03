package com.muhammedtopgul.orderservice.configuration;

import com.muhammedtopgul.application.config.BeanConfig;
import com.muhammedtopgul.application.config.JmsConfig;
import com.muhammedtopgul.application.config.SchedulingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 16:28
 */

@Configuration
@Import(value = {BeanConfig.class, JmsConfig.class, SchedulingConfig.class})
public class Config {
}
