package com.sbi.dl.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming.G
 */
public class WrongUsernameOrPasswordException extends AuthenticationException {
    public WrongUsernameOrPasswordException() {
        super("Wrong Username Or Password");
    }
}
