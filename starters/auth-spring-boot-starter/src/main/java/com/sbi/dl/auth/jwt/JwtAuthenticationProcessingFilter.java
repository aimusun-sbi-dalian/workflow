package com.sbi.dl.auth.jwt;


import com.sbi.dl.auth.constant.AuthProperties;
import com.sbi.dl.auth.jwt.constant.AuthConstant;
import com.sbi.dl.auth.jwt.exception.JwtException;
import com.sbi.dl.auth.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ming.G
 */
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final JwtSettings jwtSettings;

    private final AuthService authService;


    public JwtAuthenticationProcessingFilter(
            AuthenticationFailureHandler failureHandler,
            JwtSettings jwtSettings,
            AuthService authService,
            AuthProperties authProperties) {
        super(new SkipPathRequestMatcher(
                Arrays.asList(authProperties.getEntryPointWhiteList()),
                authProperties.getAuthorizeRequestUrl()));
        this.failureHandler = failureHandler;
        this.jwtSettings = jwtSettings;
        this.authService = authService;
    }

    private String parseJwtToken(HttpServletRequest request) {
        String tokenPayload = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        if (StringUtils.isBlank(tokenPayload)
                || tokenPayload.length() < AuthConstant.JWT_TOKEN_PREFIX.length()) {
            throw new JwtException("Token Invalid");
        }
        return tokenPayload.substring(AuthConstant.JWT_TOKEN_PREFIX.length());
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {
        String token = parseJwtToken(request);

        JwtParser jwtParser = new JwtParser(token, jwtSettings.getTokenSigningKey());

        UserDetails user = authService.tokenCheck(jwtParser.getSubject());

        List<String> scopes = jwtParser.getScopes();
        Set<GrantedAuthority> authorities =
                scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        Set<GrantedAuthority> currentAuthorities =
                user.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toSet());
        authorities.addAll(currentAuthorities);
        return new JwtAuthenticationToken(user, authorities);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
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
