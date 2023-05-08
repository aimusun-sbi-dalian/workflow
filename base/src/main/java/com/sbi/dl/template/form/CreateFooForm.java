package com.sbi.dl.template.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CreateFooForm implements Serializable {

    @NotBlank private String username;

    @NotNull private String password;

    @NotNull private Integer status;
}
