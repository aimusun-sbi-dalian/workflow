package com.sbi.expo.api.base.auth.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.api.base.ResponseBase;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * LogoutSuccessHandler
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@Slf4j
@Component
public class LogoutProcessSuccessHandler implements LogoutSuccessHandler {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // TODO Pleas add logic(redirect login page or return success)
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ResponseBase.ok());
    }
}
