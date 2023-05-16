package com.sbi.expo.cust.base.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.cust.base.ResponseBase;
import com.sbi.expo.cust.base.constant.MessageConstant;
import com.sbi.expo.cust.base.exceptions.handler.ErrorMessageHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author Ming.G
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(
                response.getWriter(),
                ResponseBase.failed(
                        Integer.parseInt(MessageConstant.CODE_10001),
                        ErrorMessageHandler.getMessage(MessageConstant.CODE_10001)));
    }
}
