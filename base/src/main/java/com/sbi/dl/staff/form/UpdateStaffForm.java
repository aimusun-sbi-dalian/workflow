package com.sbi.dl.staff.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * UpdateStaffForm
 *
 * @author Ming.G
 * @date 2022-06-06
 */
@Data
public class UpdateStaffForm {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer status;

    @NotNull private Integer version;
}
