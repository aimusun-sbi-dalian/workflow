package com.sbi.dl.template.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateFooForm {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer status;

    @NotNull private Integer version;
}
