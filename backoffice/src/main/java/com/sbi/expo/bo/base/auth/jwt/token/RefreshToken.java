package com.sbi.expo.bo.base.auth.jwt.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * RefreshToken
 *
 * @author Ming.G
 * @date 2022-01-06
 */
public class RefreshToken implements JwtToken {

    private Jws<Claims> claims;

    public RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return null;
    }

    public Jws<Claims> getClaims() {
        return claims;
    }

    public String getJti() {
        return claims.getBody().getId();
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
