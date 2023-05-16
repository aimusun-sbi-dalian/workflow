package com.sbi.expo.bo.base.rbac.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * MenuForm
 *
 * @author Ming.G
 * @date 2022-08-16
 */
@Data
public class MenuForm {

    @NotNull private Long pid;
    @NotBlank private String name;
    @NotNull private Integer sort;

    private String routing;
    @NotNull private Boolean visible;
}
