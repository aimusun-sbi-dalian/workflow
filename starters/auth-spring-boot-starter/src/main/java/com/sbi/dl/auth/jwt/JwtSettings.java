package com.sbi.dl.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtSettings {

    @NotNull private String tokenIssuer;
    @NotNull private String tokenSigningKey;

    @NotNull private Integer accessTokenExpirationTime;
    @NotNull private Integer refreshTokenExpirationTime;

    public Date calculateAccessTokenExpirationTime(Date date) {
        return getIntervalExpirationDate(date, accessTokenExpirationTime);
    }

    public Date calculateRefreshTokenExpirationTime(Date date) {
        return getIntervalExpirationDate(date, refreshTokenExpirationTime);
    }

    public Date getIntervalExpirationDate(Date date, Integer expirationTime) {
        LocalDateTime baseDateTime =
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Date.from(
                baseDateTime
                        .plusMinutes(expirationTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
