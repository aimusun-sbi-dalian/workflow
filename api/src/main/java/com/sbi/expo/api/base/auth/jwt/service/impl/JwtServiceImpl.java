package com.sbi.expo.api.base.auth.jwt.service.impl;

import com.sbi.expo.api.base.auth.jwt.JwtParser;
import com.sbi.expo.api.base.auth.jwt.JwtSettings;
import com.sbi.expo.api.base.auth.jwt.service.JwtService;
import com.sbi.expo.api.base.auth.jwt.token.JwtTokenFactory;
import com.sbi.expo.api.base.auth.jwt.token.RefreshToken;
import com.sbi.expo.api.base.auth.jwt.token.TokenPair;
import com.sbi.expo.api.base.auth.jwt.token.TokenScope;
import com.sbi.expo.api.base.constant.MessageConstant;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * JwtServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Autowired private JwtSettings jwtSettings;

    @Autowired private JwtTokenFactory jwtTokenFactory;

    /**
     * verify refresh token accuracy and effectiveness
     *
     * @param refreshToken refresh token
     * @return username after successful verification, if the verification fails then throw an
     *     authentication exception
     * @author Ming.G
     * @date 2022-05-23
     */
    @Override
    public String verifyRefreshToken(String refreshToken) {
        JwtParser jwtParser = new JwtParser(refreshToken, jwtSettings.getTokenSigningKey());

        List<String> scopes = jwtParser.getScopes();

        if (CollectionUtils.isEmpty(scopes)
                || scopes.stream()
                        .noneMatch(scope -> TokenScope.REFRESH_TOKEN.name().equals(scope))) {
            throw new BadCredentialsException(MessageConstant.CODE_10004);
        }
        RefreshToken refresh = new RefreshToken(jwtParser.getClaims());
        // verify jti
        if (StringUtils.isEmpty(refresh.getJti())) {
            log.info("[auth] refresh token jti is empty, refreshToken={}", refreshToken);
            throw new BadCredentialsException(MessageConstant.CODE_10005);
        }
        String username = refresh.getSubject();
        log.info("[auth] {} request refresh of jwt token, verified successful", username);
        return username;
    }

    /**
     * generate jwt token pair
     *
     * @param date issue time of jwt token
     * @param username subject of jwt token
     * @return token pair
     * @author Ming.G
     * @date 2022-05-23
     */
    @Override
    public TokenPair generateToken(Date date, String username) {
        return jwtTokenFactory.createJwtToken(date, username);
    }
}
