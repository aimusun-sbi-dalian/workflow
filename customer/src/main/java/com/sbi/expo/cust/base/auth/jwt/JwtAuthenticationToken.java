package com.sbi.expo.cust.base.auth.jwt;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token;

    private UserDetails userContext;

    public JwtAuthenticationToken(String unsafeToken) {
        super(null);
        this.token = unsafeToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(
            UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a"
                            + " GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
