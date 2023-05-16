package com.sbi.expo.bo.base.auth.jwt.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.auth.jwt.dto.RefreshTokenForm;
import com.sbi.expo.bo.base.auth.jwt.service.JwtService;
import com.sbi.expo.bo.base.auth.jwt.token.TokenPair;
import com.sbi.expo.bo.base.constant.AuthConstant;
import com.sbi.expo.commons.utils.MapHelper;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JwtController
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class JwtController {

    @Autowired private JwtService jwtService;

    @PostMapping("/token/refresh")
    public ResponseBase<Map<String, Object>> refreshToken(
            @RequestBody @Valid RefreshTokenForm form) {
        // check refresh token
        String username = jwtService.verifyRefreshToken(form.getRefreshToken());
        // generate new token pair
        TokenPair tokenPair = jwtService.generateToken(new Date(), username);
        log.info(
                "[auth] {} request refresh of jwt token successful, the latest token pair has been"
                        + " generated",
                username);
        return ResponseBase.ok(
                MapHelper.initMap(
                        AuthConstant.Field.ACCESS_TOKEN,
                        tokenPair.getAccessToken(),
                        AuthConstant.Field.REFRESH_TOKEN,
                        tokenPair.getRefreshToken()));
    }
}
