package com.sbi.expo.api.base.auth.jwt.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * RefreshTokenForm
 *
 * @author Ming.G
 * @date 2022-01-06
 */
@Data
public class RefreshTokenForm {

    @NotBlank private String refreshToken;
}
