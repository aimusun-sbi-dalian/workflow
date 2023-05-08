package com.sbi.dl.staff.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CreateStaffForm
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Data
public class CreateStaffForm implements Serializable {

    @NotBlank private String username;

    @NotNull private String password;

    @NotNull private Integer status;
}
