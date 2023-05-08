package com.sbi.dl.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.dl.auth.UnauthorizedEntryPoint;
import com.sbi.dl.auth.constant.AuthProperties;
import com.sbi.dl.auth.jwt.JwtAuthenticationFailureHandler;
import com.sbi.dl.auth.jwt.JwtAuthenticationProcessingFilter;
import com.sbi.dl.auth.jwt.JwtManager;
import com.sbi.dl.auth.jwt.JwtSettings;
import com.sbi.dl.auth.login.LoginAuthenticationFailureHandler;
import com.sbi.dl.auth.login.LoginAuthenticationSuccessHandler;
import com.sbi.dl.auth.login.LoginProcessingFilter;
import com.sbi.dl.auth.logout.LogoutProcessHandler;
import com.sbi.dl.auth.logout.LogoutProcessSuccessHandler;
import com.sbi.dl.auth.service.AuthService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfiguration
 *
 * @author Ming.G
 * @date 2022-07-11
 */
@AutoConfiguration
@EnableWebSecurity
@ConditionalOnBean(AuthService.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({AuthProperties.class, JwtSettings.class})
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtManager jwtManager(JwtSettings    jwtSettings) {
        return new JwtManager(jwtSettings);
    }

    @Bean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(
            ObjectMapper objectMapper, JwtManager jwtManager) {
        return new LoginAuthenticationSuccessHandler(objectMapper, jwtManager);
    }

    @Bean
    public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler(
            ObjectMapper objectMapper, AuthService authService) {
        return new LoginAuthenticationFailureHandler(objectMapper, authService);
    }

    @Bean
    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler(
            ObjectMapper objectMapper, AuthService authService) {
        return new JwtAuthenticationFailureHandler(objectMapper, authService);
    }


    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint(
            ObjectMapper objectMapper, AuthService authService) {
        return new UnauthorizedEntryPoint(objectMapper, authService);
    }

    @Bean
    public LogoutProcessHandler logoutProcessHandler(AuthService authService) {
        return new LogoutProcessHandler(authService);
    }
    @Bean
    public LogoutProcessSuccessHandler logoutProcessSuccessHandler(ObjectMapper objectMapper) {
        return new LogoutProcessSuccessHandler(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UnauthorizedEntryPoint authenticationEntryPoint,
            AuthProperties authProperties,
            LogoutProcessHandler logoutProcessHandler,
            LogoutProcessSuccessHandler logoutProcessSuccessHandler,
            LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler,
            LoginAuthenticationFailureHandler loginAuthenticationFailureHandler,
            AuthService authService,
            ObjectMapper objectMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler,
            JwtSettings jwtSettings)  throws Exception {
        http.csrf()
                .disable()
                // exception handling
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // logout
                .logout()
                .logoutUrl(authProperties.getLogout())
                .addLogoutHandler(logoutProcessHandler)
                .logoutSuccessHandler(logoutProcessSuccessHandler)
                .and()
                // session management
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // request auth -- white list
                .authorizeRequests()
                .antMatchers(authProperties.getEntryPointWhiteList())
                .permitAll()
                .and()
                // request auth -- matches all entry points
                .authorizeRequests()
                .antMatchers(authProperties.getEntryPoint())
                .authenticated()
                .and()
                // custom filter
                .addFilterBefore(
                        loginProcessingFilter(
                                authProperties,
                                loginAuthenticationSuccessHandler,
                                loginAuthenticationFailureHandler,
                                objectMapper,
                                authService,
                                bCryptPasswordEncoder), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtAuthenticationProcessingFilter(
                                authProperties,
                                jwtAuthenticationFailureHandler,
                                jwtSettings,
                                authService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private LoginProcessingFilter loginProcessingFilter(
            AuthProperties authProperties,
            LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler,
            LoginAuthenticationFailureHandler loginAuthenticationFailureHandler,
            ObjectMapper objectMapper,
            AuthService authService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new LoginProcessingFilter(
                loginAuthenticationSuccessHandler,
                loginAuthenticationFailureHandler,
                objectMapper,
                authService,
                bCryptPasswordEncoder,
                authProperties);
    }

    private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(
            AuthProperties authProperties,
            JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler,
            JwtSettings jwtSettings,
            AuthService authService) {
        return new JwtAuthenticationProcessingFilter(
                jwtAuthenticationFailureHandler,
                jwtSettings,
                authService,
                authProperties);
    }
}
