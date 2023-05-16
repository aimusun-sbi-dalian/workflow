package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * ModifyStaffForm
 *
 * @author Ming.G
 * @date 2022-06-06
 */
@Data
public class ModifyStaffForm {

    @NotNull @Min(value = 0)
    @Max(value = 1)
    private Integer status;

    @NotNull private Long roleId;

    @NotNull private Integer version;
}
