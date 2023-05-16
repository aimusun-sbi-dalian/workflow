package com.sbi.expo.cust.base.auth.jwt.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPair {
    String accessToken;
    String refreshToken;
}
