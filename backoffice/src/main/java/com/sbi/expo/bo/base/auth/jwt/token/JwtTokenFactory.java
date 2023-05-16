package com.sbi.expo.bo.base.auth.jwt.token;

import com.sbi.expo.bo.base.auth.jwt.JwtSettings;
import com.sbi.expo.bo.base.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFactory {

    @Autowired private JwtSettings settings;

    public TokenPair createJwtToken(Date date, String username) {
        return TokenPair.builder()
                .accessToken(createAccessJwtToken(date, username).getToken())
                .refreshToken(createRefreshToken(date, username).getToken())
                .build();
    }

    public JwtToken createAccessJwtToken(Date date, String username) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AuthConstant.Field.SCOPES, new String[] {TokenScope.ACCESS_TOKEN.name()});
        String token =
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuer(settings.getTokenIssuer())
                        .setIssuedAt(date)
                        .setExpiration(settings.calculateAccessTokenExpirationTime(date))
                        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                        .compact();

        return new AccessToken(token, claims);
    }

    public JwtToken createRefreshToken(Date date, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AuthConstant.Field.SCOPES, new String[] {TokenScope.REFRESH_TOKEN.name()});
        String token =
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuer(settings.getTokenIssuer())
                        .setId(UUID.randomUUID().toString())
                        .setIssuedAt(date)
                        .setExpiration(settings.calculateRefreshTokenExpirationTime(date))
                        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                        .compact();

        return new AccessToken(token, claims);
    }
}
