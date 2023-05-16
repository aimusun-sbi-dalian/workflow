package com.sbi.expo.bo.base.auth.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * LogoutProcessHandler
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@Slf4j
@Component
public class LogoutProcessHandler implements LogoutHandler {

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        // TODO Please add logout logic
    }
}
