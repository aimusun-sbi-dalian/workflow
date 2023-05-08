package com.sbi.dl.auth.jwt;

import com.sbi.dl.auth.jwt.constant.AuthConstant;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

/**
 * JwtParser
 *
 * @author Ming.G
 * @date 2022-05-23
 */
public class JwtParser {

    private final String token;

    private final String signingKey;
    @Getter private final Jws<Claims> claims;

    public JwtParser(String token, String signingKey) {
        this.token = token;
        this.signingKey = signingKey;
        this.claims = parseClaims();
    }

    public Jws<Claims> parseClaims() {
        try {
            return Jwts.parser().setSigningKey(this.signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException |
                 ExpiredJwtException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    public List<String> getScopes() {
        return claims.getBody().get(AuthConstant.JWT_SCOPES, List.class);
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
