//package com.sbi.dl.oauth2.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Configuration
//@EnableWebSecurity
//public class OAuth2SecurityConfig {
//
//
//    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            return mappedAuthorities;
//        };
//    }
//
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
////                        .clientRegistrationRepository(this.clientRegistrationRepository())
////                        .authorizedClientRepository(this.authorizedClientRepository())
////                        .authorizedClientService(this.authorizedClientService())
//                        .loginPage("/login")
//                        .authorizationEndpoint(authorization -> authorization
//                                .baseUri("/login/oauth2/authorization"))
////                                .authorizationRequestRepository(this.authorizationRequestRepository())
////                                .authorizationRequestResolver(this.authorizationRequestResolver()))
//                        .redirectionEndpoint(redirection -> redirection
//                                .baseUri("/login/oauth2/callback/*")
//                        )
//                        .tokenEndpoint(token -> token
//                                .accessTokenResponseClient(this.accessTokenResponseClient())
//                        )
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userAuthoritiesMapper(this.userAuthoritiesMapper())
//                                .userService(this.oauth2UserService())
//                                .oidcUserService(this.oidcUserService())
//                        )
//                );
//        return http.build();
//    }
//
//
//}