package com.muhammedtopgul.beerservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author muhammed-topgul
 * @since 27.02.2022 20:52
 */

@Configuration
@EnableScheduling
@EnableAsync
public class TaskConfig {

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
