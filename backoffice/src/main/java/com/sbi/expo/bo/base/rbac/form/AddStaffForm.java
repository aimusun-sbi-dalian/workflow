package com.sbi.expo.bo.base.rbac.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * AddStaffForm
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Data
public class AddStaffForm implements Serializable {

    @NotBlank private String name;

    @NotNull private Long roleId;
}
