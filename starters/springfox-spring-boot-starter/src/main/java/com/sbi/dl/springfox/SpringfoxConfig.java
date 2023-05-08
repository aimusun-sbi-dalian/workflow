package com.sbi.dl.springfox;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringfoxConfig
 *
 * @author Ming.G
 * @date 2022-10-12
 */
@Getter
@Setter
@ConfigurationProperties("sbibits.springfox")
public class SpringfoxConfig {

    private boolean enabled = false;

    private String basePackage;

    private String title;

    private String description;

    private String version;
}
