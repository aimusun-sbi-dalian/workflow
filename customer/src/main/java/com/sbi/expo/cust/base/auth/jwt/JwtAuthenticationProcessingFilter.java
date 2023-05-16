package com.sbi.expo.cust.base.auth.jwt;

import com.sbi.expo.commons.utils.WebHelper;
import com.sbi.expo.cust.base.auth.SkipPathRequestMatcher;
import com.sbi.expo.cust.base.constant.AuthConstant;
import com.sbi.expo.cust.base.constant.MessageConstant;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author Ming.G
 */
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final JwtSettings jwtSettings;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProcessingFilter(
            AuthenticationFailureHandler failureHandler,
            JwtSettings jwtSettings,
            UserDetailsService userDetailsService) {
        super(
                new SkipPathRequestMatcher(
                        Arrays.asList(AuthConstant.EntryPoint.ENTRY_POINT_WHITE_LIST),
                        AuthConstant.EntryPoint.ALL));
        this.failureHandler = failureHandler;
        this.jwtSettings = jwtSettings;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {

        String tokenPayload = request.getHeader(WebHelper.JWT_TOKEN_HEADER);
        if (StringUtils.isBlank(tokenPayload)) {
            throw new AuthenticationServiceException(MessageConstant.CODE_10006);
        }

        if (tokenPayload.length() < WebHelper.JWT_TOKEN_PREFIX.length()) {
            throw new AuthenticationServiceException(MessageConstant.CODE_10007);
        }
        String token = tokenPayload.substring(WebHelper.JWT_TOKEN_PREFIX.length());

        JwtParser jwtParser = new JwtParser(token, jwtSettings.getTokenSigningKey());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtParser.getSubject());
        List<String> scopes = jwtParser.getScopes();
        Set<GrantedAuthority> authorities =
                scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        if (userDetails == null) {
            throw new BadCredentialsException(MessageConstant.CODE_10004);
        }
        // check status
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException(MessageConstant.CODE_10003);
        }
        Set<GrantedAuthority> currentAuthorities =
                userDetails.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toSet());
        authorities.addAll(currentAuthorities);
        return new JwtAuthenticationToken(userDetails, authorities);
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
