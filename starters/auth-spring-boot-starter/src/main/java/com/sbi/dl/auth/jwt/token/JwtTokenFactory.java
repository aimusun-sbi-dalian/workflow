package com.sbi.dl.auth.jwt.token;


import com.sbi.dl.auth.jwt.JwtSettings;
import com.sbi.dl.auth.jwt.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtTokenFactory {

    public static TokenPair createJwtToken(Date date, String username, JwtSettings settings) {
        return TokenPair.builder()
                .accessToken(createAccessJwtToken(date, username, settings).getToken())
                .refreshToken(createRefreshToken(date, username, settings).getToken())
                .build();
    }

    public static JwtToken createAccessJwtToken(Date date, String username, JwtSettings settings) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AuthConstant.JWT_SCOPES, new String[] {TokenScope.ACCESS_TOKEN.name()});
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

    public static JwtToken createRefreshToken(Date date, String username, JwtSettings settings) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AuthConstant.JWT_SCOPES, new String[] {TokenScope.REFRESH_TOKEN.name()});
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
