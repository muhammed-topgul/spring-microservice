package com.muhammedtopgul.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author muhammed-topgul
 * @since 28.02.2022 16:19
 */

@Configuration
@EnableScheduling
@EnableAsync
public class SchedulerTaskConfig {

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
