package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ModifyStaffPasswordForm
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
public class ModifyStaffPasswordForm {
    @NotBlank private String oldPassword;

    @NotBlank private String newPassword;
}
