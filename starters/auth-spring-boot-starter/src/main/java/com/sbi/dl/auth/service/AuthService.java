package com.sbi.dl.auth.service;

import com.sbi.dl.auth.exception.UserNotEnabledException;
import com.sbi.dl.auth.exception.WrongUsernameOrPasswordException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public interface AuthService extends UserDetailsService {
    String USERNAME = "username";
    String PASSWORD = "password";

    default UserDetails tokenCheck(String username) throws BadCredentialsException {
        UserDetails user = loadUserByUsername(username);
        if (Objects.isNull(user)) {
            userNotFoundProcess();
        }
        // check user status
        if (!user.isEnabled()) {
            notEnabledProcess();
        }
        return user;
    }

    default UserDetails loginCheck(
            Map<String, String> loginRequest, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws BadCredentialsException {
        String username = loginRequest.get(USERNAME);
        UserDetails user = loadUserByUsername(username);
        if (Objects.isNull(user)) {
            userNotFoundProcess();
        }
        // check allow account password
        if (!bCryptPasswordEncoder.matches(loginRequest.get(PASSWORD), user.getPassword())) {
            wrongPasswordProcess();
        }
        // check user status
        if (!user.isEnabled()) {
            notEnabledProcess();
        }
        return user;
    }

    default void userNotFoundProcess() {
        throw new UsernameNotFoundException("User Not Found");
    }

    default void wrongPasswordProcess() {
        throw new WrongUsernameOrPasswordException();
    }

    default void notEnabledProcess() {
        throw new UserNotEnabledException();
    }

    default void jwtAuthFailedProcess(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) {
        // default do nothing
    }

    default void loginFailedProcess(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) {
        // default do nothing
    }

    default void logoutProcess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        // default do nothing
    }

    Object loginFailedResponseData(AuthenticationException exception);

    Object jwtAuthFailedResponseData(AuthenticationException exception);

    Object unauthorizedResponseData(AuthenticationException exception);



}
