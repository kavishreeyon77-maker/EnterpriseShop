package com.enterprise.cartcheckout.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Uses default Spring Boot TaskExecutor configuration which is generally sufficient.
}
