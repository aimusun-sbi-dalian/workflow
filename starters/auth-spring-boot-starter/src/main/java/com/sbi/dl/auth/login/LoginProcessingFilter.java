package com.sbi.dl.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.dl.auth.constant.AuthProperties;
import com.sbi.dl.auth.exception.WrongUsernameOrPasswordException;
import com.sbi.dl.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.el.MethodNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ming.G
 */
@Slf4j
public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginProcessingFilter(
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            ObjectMapper mapper,
            AuthService authService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthProperties authProperties) {
        super(authProperties.getLogin());
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.authService = authService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Method Not Found", new MethodNotFoundException());
        }

        try {
            Map loginRequest = objectMapper.readValue(request.getReader(), Map.class);
            if (MapUtils.isEmpty(loginRequest)) {
                throw new WrongUsernameOrPasswordException();
            }
            UserDetails user = authService.loginCheck(loginRequest, bCryptPasswordEncoder);
            return UsernamePasswordAuthenticationToken.authenticated(
                    user, user.getPassword(), user.getAuthorities());
        } catch (IOException e) {
            throw new WrongUsernameOrPasswordException();
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
