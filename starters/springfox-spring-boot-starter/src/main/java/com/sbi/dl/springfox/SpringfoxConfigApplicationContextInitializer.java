package com.sbi.dl.springfox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * SpringfoxConfigApplicationContextInitializer
 *
 * @author Ming.G
 * @date 2022-10-13
 */
@Slf4j
public class SpringfoxConfigApplicationContextInitializer implements ApplicationContextInitializer {

    private static final String STRATEGY = "spring.mvc.pathmatch.matching-strategy";
    private static final String STRATEGY_VALUE = "ant_path_matcher";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        final Map<String, Object> map = new HashMap<>(2);
        map.put(STRATEGY, STRATEGY_VALUE);
        applicationContext
                .getEnvironment()
                .getPropertySources()
                .addLast(new MapPropertySource("springfox-config", map));
        log.info("Set '{}' default value is {}", STRATEGY, STRATEGY_VALUE);
    }
}
