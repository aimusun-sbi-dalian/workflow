package com.sbi.expo.api.base.auth.login;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.api.base.constant.AuthConstant;
import com.sbi.expo.api.base.constant.MessageConstant;
import com.sbi.expo.commons.utils.WebHelper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Ming.G
 */
@Slf4j
public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginProcessingFilter(
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            ObjectMapper mapper,
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(AuthConstant.EntryPoint.LOGIN);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!WebHelper.isPostRequest(request)) {
            throw new AuthenticationServiceException(MessageConstant.CODE_10001);
        }

        try {
            LoginRequest loginRequest =
                    objectMapper.readValue(request.getReader(), LoginRequest.class);

            if (StringUtils.isAnyBlank(loginRequest.getUsername(), loginRequest.getPassword())) {
                throw new AuthenticationServiceException(MessageConstant.CODE_10002);
            }
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (user == null) {
                log.info("[login user] {} does not exist", loginRequest.getUsername());
                throw new BadCredentialsException(MessageConstant.CODE_10002);
            }

            // check password
            if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.info(
                        "[login user] {} entered the password incorrectly",
                        loginRequest.getUsername());
                throw new BadCredentialsException(MessageConstant.CODE_10002);
            }
            // check status
            if (!user.isEnabled()) {
                throw new BadCredentialsException(MessageConstant.CODE_10003);
            }

            return new UsernamePasswordAuthenticationToken(
                    user, loginRequest.getPassword(), user.getAuthorities());
        } catch (JsonParseException | JsonMappingException e) {
            throw new AuthenticationServiceException(MessageConstant.CODE_10002);
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
