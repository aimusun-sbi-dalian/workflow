package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * ModifyPermissionForm
 *
 * @author Ming.G
 * @date 2022-07-29
 */
@Data
public class ModifyPermissionForm {

    @NotNull private Long pid;
    @NotBlank private String key;
    @NotBlank private String name;

    @Max(1)
    @Min(0)
    @NotNull private Integer type;

    @NotNull private Integer version;
}
