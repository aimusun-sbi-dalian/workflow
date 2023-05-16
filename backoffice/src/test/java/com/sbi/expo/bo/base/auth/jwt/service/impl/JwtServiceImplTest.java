package com.sbi.expo.bo.base.auth.jwt.service.impl;

import com.sbi.expo.bo.base.auth.jwt.JwtSettings;
import com.sbi.expo.bo.base.auth.jwt.token.JwtTokenFactory;
import com.sbi.expo.bo.base.auth.jwt.token.TokenPair;
import com.sbi.expo.bo.base.config.ExtensionContextParameterResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {JwtServiceImpl.class, JwtSettings.class,JwtTokenFactory.class})
@ExtendWith({SpringExtension.class, ExtensionContextParameterResolver.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtServiceImplTest {

    @Autowired private JwtServiceImpl jwtServiceImpl;

    @Autowired JwtSettings jwtSettings;

    Date date;

    private String userName = "tester";

    @BeforeAll
    void setUp(ExtensionContext ex){
        ApplicationContext ctx =
                SpringExtension.getApplicationContext(ex);
        JwtSettings settings = ctx.getBean(JwtSettings.class);
        settings.setTokenSigningKey("vxk9D2cAeTsMgNpo7tZKWwFlQmzRBObU");
        settings.setTokenIssuer("EXPO2025");
        settings.setAccessTokenExpirationTime(240);
        settings.setRefreshTokenExpirationTime(720);

        LocalDateTime dateTime = LocalDateTime.of(2099,1,1,0,0,0);
        Instant instant = dateTime.atZone(ZoneId.of("Asia/Tokyo")).toInstant();
        date = Date.from(instant);
    }

    @Test
    public void testVerifyRefreshToken_noScopes() {
        assertThrows(BadCredentialsException.class, () -> {
            String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
            jwtServiceImpl.verifyRefreshToken(refreshToken);
        });
    }

    @Test
    public void testVerifyRefreshToken_success() {
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0ZXIiLCJzY29wZXMiOlsiUkVGUkVTSF9UT0tFTiJdLCJpc3MiOiJFWFBPMjAyNSIsImp0aSI6IjgyMzBiZjM3LWVhNzctNDJkOS04Yzg2LWVhZDllYzliN2U0NSIsImlhdCI6NDA3MDg3NjQwMCwiZXhwIjo0MDcwOTE5NjAwfQ.ZmJXPveAbgu44J_eFjXOesNcOyd1lC8mQ9bILcCmc_uDXg50nmApqXt50B4pO9TCznuJW_8-5CbQy0CNkSmdng";
        String username = jwtServiceImpl.verifyRefreshToken(refreshToken);
        assertEquals(userName, username);
    }

    @Test
    public void testGenerateToken1(){
        TokenPair token = jwtServiceImpl.generateToken(date,userName);
        assertEquals(token.getAccessToken(),"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0ZXIiLCJzY29wZXMiOlsiQUNDRVNTX1RPS0VOIl0sImlzcyI6IkVYUE8yMDI1IiwiaWF0Ijo0MDcwODc2NDAwLCJleHAiOjQwNzA4OTA4MDB9.tdZzIo-PtIJGww_otl6afIRSUVZzPpCtIx9ZJBRVZch81B5nWrzQj_mHxJTwllf7ixRyxnnSb5WNfYjLr6STxg");
        //can not assert refresh token, it contains uuid
    }



}
