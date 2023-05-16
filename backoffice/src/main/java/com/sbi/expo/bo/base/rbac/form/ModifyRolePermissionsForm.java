package com.sbi.expo.bo.base.rbac.form;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * ModifyRolePermissionsForm
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
public class ModifyRolePermissionsForm {
    @NotNull private List<Long> permissionIds;
}
