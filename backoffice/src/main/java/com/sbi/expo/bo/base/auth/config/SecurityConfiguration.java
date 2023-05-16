package com.sbi.expo.bo.base.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.bo.base.auth.UnauthorizedEntryPoint;
import com.sbi.expo.bo.base.auth.jwt.JwtAuthenticationProcessingFilter;
import com.sbi.expo.bo.base.auth.jwt.JwtSettings;
import com.sbi.expo.bo.base.auth.login.LoginProcessingFilter;
import com.sbi.expo.bo.base.constant.AuthConstant;
import com.sbi.expo.bo.base.rbac.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * SecurityConfiguration
 *
 * @author Ming.G
 * @date 2022-07-11
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired private UnauthorizedEntryPoint authenticationEntryPoint;
    @Autowired private LogoutHandler logoutProcessHandler;
    @Autowired private LogoutSuccessHandler logoutProcessSuccessHandler;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    @Autowired private AuthenticationFailureHandler loginAuthenticationFailureHandler;
    @Autowired private AuthenticationFailureHandler jwtAuthenticationFailureHandler;
    @Autowired private AuthService authService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private JwtSettings jwtSettings;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                // exception handling
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // logout
                .logout()
                .logoutUrl(AuthConstant.EntryPoint.LOGOUT)
                .addLogoutHandler(logoutProcessHandler)
                .logoutSuccessHandler(logoutProcessSuccessHandler)
                .and()
                // session management
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // request auth -- white list
                .authorizeRequests()
                .antMatchers(AuthConstant.EntryPoint.ENTRY_POINT_WHITE_LIST)
                .permitAll()
                .and()
                // request auth -- matches all entry points
                .authorizeRequests()
                .antMatchers(AuthConstant.EntryPoint.ALL)
                .authenticated()
                .and()
                // custom filter
                .addFilterBefore(
                        loginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtAuthenticationProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private LoginProcessingFilter loginProcessingFilter() {
        return new LoginProcessingFilter(
                loginAuthenticationSuccessHandler,
                loginAuthenticationFailureHandler,
                objectMapper,
                authService,
                bCryptPasswordEncoder);
    }

    private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(
                jwtAuthenticationFailureHandler, jwtSettings, authService);
    }
}
