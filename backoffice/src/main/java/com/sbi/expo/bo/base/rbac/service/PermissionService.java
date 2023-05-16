package com.sbi.expo.bo.base.rbac.service;

import com.sbi.expo.bo.base.rbac.dto.PermissionsTreeDTO;
import com.sbi.expo.bo.base.rbac.form.AddPermissionForm;
import com.sbi.expo.bo.base.rbac.form.ModifyPermissionForm;
import com.sbi.expo.bo.base.rbac.model.Permission;
import java.util.List;

/**
 * PermissionService
 *
 * @author Ming.G
 * @date 2022-05-25
 */
public interface PermissionService {

    /**
     * get all permissions tree
     *
     * @return List<PermissionsTreeDTO>
     */
    List<PermissionsTreeDTO> permissionsTree();

    /**
     * get all permissions id
     *
     * @return List<Long>
     */
    List<Long> getAllPermissionIds();

    /**
     * get permission list by permission ids
     *
     * @param permissionIds permissionIds
     * @return List<Permission>
     */
    List<Permission> getPermissions(List<Long> permissionIds);

    /**
     * create permission
     *
     * @param form AddPermissionForm
     */
    void create(AddPermissionForm form);

    /**
     * modify permission
     *
     * @param permissionId permission id
     * @param form ModifyPermissionForm
     */
    void modify(Long permissionId, ModifyPermissionForm form);

    /**
     * delete permission
     *
     * @param permissionId permission id
     */
    void delete(Long permissionId);
}
