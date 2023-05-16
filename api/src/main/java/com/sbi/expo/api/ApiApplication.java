package com.sbi.expo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.sbi.expo.api.*"})
@EnableJpaRepositories(basePackages = {"com.sbi.expo.api.*"})
@SpringBootApplication(scanBasePackages = {"com.sbi.expo.api.*"})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
