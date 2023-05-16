package com.sbi.expo.bo.base.auth.login;

import com.sbi.expo.bo.base.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Ming.G
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired private UserDetailsService authService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        return auth(authentication);
    }

    private Authentication auth(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        UserDetails user = authService.loadUserByUsername(username);
        if (user == null) {
            log.info("[login] Staff({}) does not exist", username);
            throw new BadCredentialsException(MessageConstant.CODE_10002);
        }

        String password = (String) authentication.getCredentials();
        // check password
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            log.info("[login] Staff({}) entered the password incorrectly", username);
            throw new BadCredentialsException(MessageConstant.CODE_10002);
        }
        // check status
        if (!user.isEnabled()) {
            throw new BadCredentialsException(MessageConstant.CODE_10003);
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
