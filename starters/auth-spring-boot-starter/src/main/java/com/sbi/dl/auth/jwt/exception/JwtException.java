package com.sbi.dl.auth.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(){
        super("Jwt Exception");
    }
}
