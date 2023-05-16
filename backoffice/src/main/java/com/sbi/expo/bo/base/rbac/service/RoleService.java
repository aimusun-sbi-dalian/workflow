package com.sbi.expo.bo.base.rbac.service;

import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.dto.RoleDTO;
import com.sbi.expo.bo.base.rbac.model.Role;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * RoleService
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface RoleService {
    Role getRole(Long roleId);

    void createRole(String roleName);

    void modifyRole(Long roleId, String roleName, Integer version);

    void deleteRole(Long roleId);

    Page<RoleDTO> getRoleListDTO(Pageable pageable);

    RoleDTO getRoleDTO(Long roleId);

    List<Long> getRolePermissionIds(Long roleId);

    void modifyRolePermissions(Long roleId, List<Long> permissionIds);

    boolean isAdmin(Long id);

    List<Long> getRoleMenuIds(Long roleId);

    void modifyRoleMenus(Long roleId, List<Long> menuIds);

    List<MenuTreeDTO> getRoleMenuTree(Long id);
}
