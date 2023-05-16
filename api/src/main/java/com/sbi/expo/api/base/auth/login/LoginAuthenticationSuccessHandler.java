package com.sbi.expo.api.base.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.api.base.ResponseBase;
import com.sbi.expo.api.base.auth.jwt.service.JwtService;
import com.sbi.expo.api.base.auth.jwt.token.TokenPair;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private ObjectMapper mapper;
    @Autowired private JwtService jwtService;

    /** generate token and respond to the client */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserDetails userContext = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        // generate jwt token
        TokenPair tokenPair = jwtService.generateToken(now, userContext.getUsername());
        // respond to the client
        respond2Client(response, tokenPair);
    }

    private void respond2Client(HttpServletResponse response, TokenPair tokenPair)
            throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), ResponseBase.ok(tokenPair));
    }
}
