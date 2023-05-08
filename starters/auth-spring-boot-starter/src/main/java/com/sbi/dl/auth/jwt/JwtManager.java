package com.sbi.dl.auth.jwt;

import com.sbi.dl.auth.jwt.exception.JwtException;
import com.sbi.dl.auth.jwt.token.JwtTokenFactory;
import com.sbi.dl.auth.jwt.token.RefreshToken;
import com.sbi.dl.auth.jwt.token.TokenPair;
import com.sbi.dl.auth.jwt.token.TokenScope;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
public class JwtManager {

    private final JwtSettings jwtSettings;

    @PostConstruct
    public void post() {
        if (StringUtils.isBlank(jwtSettings.getTokenIssuer())) {
            throw new JwtException("Please add config");
        }
        if (StringUtils.isBlank(jwtSettings.getTokenSigningKey())) {
            throw new JwtException("Please add config");
        }
    }

    /**
     * refresh token
     *
     * @param refreshToken refresh token
     */
    public TokenPair refreshToken(String refreshToken) throws JwtException {
        // verify refresh token accuracy and effectiveness
        JwtParser jwtParser = new JwtParser(refreshToken, jwtSettings.getTokenSigningKey());

        List<String> scopes = jwtParser.getScopes();

        if (CollectionUtils.isEmpty(scopes)
                || scopes.stream()
                .noneMatch(scope -> TokenScope.REFRESH_TOKEN.name().equals(scope))) {
            throw new JwtException("Invalid Token");
        }
        RefreshToken refresh = new RefreshToken(jwtParser.getClaims());
        // verify jti
        if (StringUtils.isEmpty(refresh.getJti())) {
            throw new JwtException("Expired Token");
        }
        return issueToken(new Date(), refresh.getSubject());
    }

    public TokenPair issueToken(Date date, String username) {
        return JwtTokenFactory.createJwtToken(date, username, jwtSettings);
    }

}
