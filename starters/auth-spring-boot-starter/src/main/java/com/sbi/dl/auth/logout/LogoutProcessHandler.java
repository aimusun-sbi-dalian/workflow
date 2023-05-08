package com.sbi.dl.auth.logout;

import com.sbi.dl.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutProcessHandler
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@RequiredArgsConstructor
public class LogoutProcessHandler implements LogoutHandler {

    private final AuthService authService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        // TODO Please add logout logic
        authService.logoutProcess(request, response, authentication);
    }
}
