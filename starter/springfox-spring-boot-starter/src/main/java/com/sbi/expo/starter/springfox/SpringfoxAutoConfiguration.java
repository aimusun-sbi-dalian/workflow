package com.sbi.expo.starter.springfox;

import java.util.*;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SpringfoxAutoConfiguration
 *
 * @author Ming.G
 * @date 2022-10-12
 */
@EnableOpenApi
@AutoConfiguration
@ConditionalOnClass({Docket.class, HttpMethod.class})
@EnableConfigurationProperties(SpringfoxConfig.class)
public class SpringfoxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Docket docket(SpringfoxConfig config) {
        Docket docket =
                new Docket(DocumentationType.OAS_30)
                        .apiInfo(
                                new ApiInfoBuilder()
                                        .title(config.getTitle())
                                        .description(config.getDescription())
                                        .version(config.getVersion())
                                        .build())
                        // enable swagger
                        .enable(config.isEnabled())
                        .select()
                        // scan package
                        .apis(RequestHandlerSelectors.basePackage(config.getBasePackage()))
                        // handle paths
                        .paths(PathSelectors.any())
                        .build();

        if (config.isEnableAuthorize()) {
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
            SecurityContext securityContext =
                    SecurityContext.builder()
                            .securityReferences(
                                    List.of(
                                            new SecurityReference(
                                                    config.getApiKeyName(), authorizationScopes)))
                            .build();
            // the keyname parameter is ignored
            ApiKey apiKey = new ApiKey(config.getApiKeyName(), "Authorization", "header");
            docket.securityContexts(Collections.singletonList(securityContext))
                    .securitySchemes(List.of(apiKey));
        }

        return docket.useDefaultResponseMessages(false);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier,
            ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier,
            EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties,
            WebEndpointProperties webEndpointProperties,
            Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping =
                this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(
                endpointMapping,
                webEndpoints,
                endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping,
                null);
    }

    private boolean shouldRegisterLinksMapping(
            WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled()
                && (StringUtils.hasText(basePath)
                        || ManagementPortType.get(environment)
                                .equals(ManagementPortType.DIFFERENT));
    }
}
