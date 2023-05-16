package com.sbi.expo.bo.base.auth.jwt.service;

import com.sbi.expo.bo.base.auth.jwt.token.TokenPair;
import java.util.Date;

/**
 * JwtService
 *
 * @author Ming.G
 * @date 2022-05-23
 */
public interface JwtService {

    /**
     * verify refresh token accuracy and effectiveness
     *
     * @param refreshToken refresh token
     * @return username after successful verification, if the verification fails then throw an
     *     authentication exception
     * @author Ming.G
     * @date 2022-05-23
     */
    String verifyRefreshToken(String refreshToken);

    /**
     * generate jwt token pair
     *
     * @param date issue time of jwt token
     * @param username subject of jwt token
     * @return token pair
     * @author Ming.G
     * @date 2022-05-23
     */
    TokenPair generateToken(Date date, String username);
}
