package com.sbi.expo.api.base.auth.jwt;

import com.sbi.expo.api.base.constant.AuthConstant;
import com.sbi.expo.api.base.constant.MessageConstant;
import io.jsonwebtoken.*;
import java.util.List;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

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
        } catch (UnsupportedJwtException
                | MalformedJwtException
                | IllegalArgumentException
                | SignatureException ex) {
            throw new BadCredentialsException(MessageConstant.CODE_10004);
        } catch (ExpiredJwtException expiredEx) {
            throw new BadCredentialsException(MessageConstant.CODE_10005);
        }
    }

    public List<String> getScopes() {
        return claims.getBody().get(AuthConstant.Field.SCOPES, List.class);
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
