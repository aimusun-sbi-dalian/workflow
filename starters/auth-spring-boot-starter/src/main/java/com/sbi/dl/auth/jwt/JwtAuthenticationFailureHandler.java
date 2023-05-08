package com.sbi.dl.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.dl.auth.service.AuthService;
import com.sbi.dl.compoment.ResponseBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Ming.G
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;

    private final AuthService authService;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        authService.jwtAuthFailedProcess(request, response, exception);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        mapper.writeValue(response.getWriter(),
                ResponseBase.failed(HttpStatus.UNAUTHORIZED.value(),
                        authService.jwtAuthFailedResponseData(exception)));
    }
}
