package com.sbi.expo.bo.base.rbac.form;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * ModifyRoleMenusForm
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
public class ModifyRoleMenusForm {
    @NotNull private List<Long> menuIds;
}
