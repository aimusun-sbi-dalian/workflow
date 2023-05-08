package com.sbi.dl.auth.constant;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author aimu.sun
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "auth.uri")
public class AuthProperties {

    public String[] entryPointWhiteList = {"/pub/*", "/public/*"};

    public String entryPoint = "/**";

    public String login = "/int/v1/login";

    public String logout = "/int/v1/logout";

    private String authorizeRequestUrl = "/**";

}
