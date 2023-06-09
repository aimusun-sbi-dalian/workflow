package com.sbi.expo.cust.base.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@ConfigurationProperties(prefix = "filters.cors")
public class CorsFilterConfig extends CorsConfiguration {

    @Getter @Setter private String path = "/**";
}
