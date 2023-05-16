package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AddRoleForm
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
public class AddRoleForm {
    @NotBlank private String roleName;
}
