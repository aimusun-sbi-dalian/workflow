package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * ModifyRoleForm
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
public class ModifyRoleForm {

    @NotBlank private String roleName;
    /** role version */
    @NotNull private Integer version;
}
