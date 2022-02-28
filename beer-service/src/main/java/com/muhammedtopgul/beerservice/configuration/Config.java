package com.muhammedtopgul.beerservice.configuration;

import com.muhammedtopgul.application.config.BeanConfig;
import com.muhammedtopgul.application.config.CacheConfig;
import com.muhammedtopgul.application.config.JmsConfig;
import com.muhammedtopgul.application.config.SchedulerTaskConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 16:21
 */

@Configuration
@Import(value = {BeanConfig.class, CacheConfig.class, JmsConfig.class, SchedulerTaskConfig.class})
public class Config {
}
