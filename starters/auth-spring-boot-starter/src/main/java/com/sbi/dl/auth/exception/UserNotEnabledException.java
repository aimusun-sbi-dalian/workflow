package com.sbi.dl.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming.G
 */
public class UserNotEnabledException extends AuthenticationException {
    public UserNotEnabledException() {
        super("User is not enabled");
    }
}
