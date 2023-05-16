package com.sbi.expo.cust;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.sbi.expo.cust.*"})
@EnableJpaRepositories(basePackages = {"com.sbi.expo.cust.*"})
@SpringBootApplication(scanBasePackages = {"com.sbi.expo.cust.*"})
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
