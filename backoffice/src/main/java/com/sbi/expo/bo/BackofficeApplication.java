package com.sbi.expo.bo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.sbi.expo.bo.*"})
@EnableJpaRepositories(basePackages = {"com.sbi.expo.bo.*"})
@SpringBootApplication(scanBasePackages = {"com.sbi.expo.bo.*"})
public class BackofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackofficeApplication.class, args);
    }
}
