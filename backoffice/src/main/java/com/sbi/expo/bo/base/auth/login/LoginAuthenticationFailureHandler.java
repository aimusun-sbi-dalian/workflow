package com.sbi.expo.bo.base.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.handler.ErrorMessageHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @author Ming.G
 */
@Slf4j
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String messageOrCode = exception.getMessage();
        int code = Integer.parseInt(MessageConstant.CODE_10000);
        String errorMsg = ErrorMessageHandler.getMessage(messageOrCode);
        if (StringUtils.isNotEmpty(errorMsg)) {
            code = Integer.parseInt(messageOrCode);
            messageOrCode = errorMsg;
        }
        objectMapper.writeValue(response.getWriter(), ResponseBase.failed(code, messageOrCode));
    }
}
