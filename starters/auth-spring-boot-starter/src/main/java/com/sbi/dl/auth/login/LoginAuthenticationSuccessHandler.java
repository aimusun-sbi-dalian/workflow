package com.sbi.dl.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.dl.auth.jwt.JwtManager;
import com.sbi.dl.auth.jwt.token.TokenPair;
import com.sbi.dl.compoment.ResponseBase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper mapper;
    private final JwtManager jwtService;

    /** generate token and respond to the client */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserDetails userContext = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        // generate jwt token
        TokenPair tokenPair = jwtService.issueToken(now, userContext.getUsername());
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
