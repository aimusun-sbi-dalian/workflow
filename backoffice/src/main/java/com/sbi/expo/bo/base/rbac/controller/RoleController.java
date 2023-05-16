package com.sbi.expo.bo.base.rbac.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.dto.RoleDTO;
import com.sbi.expo.bo.base.rbac.form.AddRoleForm;
import com.sbi.expo.bo.base.rbac.form.ModifyRoleForm;
import com.sbi.expo.bo.base.rbac.form.ModifyRoleMenusForm;
import com.sbi.expo.bo.base.rbac.form.ModifyRolePermissionsForm;
import com.sbi.expo.bo.base.rbac.service.RoleService;
import com.sbi.expo.commons.CommonConstant;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * RoleController
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@RestController
@RequestMapping("/int/v1/role")
public class RoleController {

    @Autowired private RoleService roleService;

    @PostMapping("")
    @PreAuthorize("@auth.check('sys:role:create')")
    public ResponseBase<Void> createRole(@RequestBody @Valid AddRoleForm roleForm) {
        roleService.createRole(roleForm.getRoleName());
        return ResponseBase.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@auth.check('sys:role:modify')")
    public ResponseBase<Void> modify(
            @PathVariable("id") Long id, @RequestBody @Valid ModifyRoleForm form) {
        roleService.modifyRole(id, form.getRoleName(), form.getVersion());
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.check('sys:role:delete')")
    public ResponseBase<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseBase.ok();
    }

    @GetMapping("")
    @PreAuthorize("@auth.check('sys:role:read')")
    public ResponseBase<Page<RoleDTO>> getRoleList(
            @PageableDefault(sort = {CommonConstant.ID}) Pageable pageable) {
        return ResponseBase.ok(roleService.getRoleListDTO(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@auth.check('sys:role:read')")
    public ResponseBase<RoleDTO> getRole(@PathVariable("id") Long id) {
        return ResponseBase.ok(roleService.getRoleDTO(id));
    }

    @GetMapping("/{id}/permission")
    @PreAuthorize("@auth.check('sys:role:permission:read')")
    public ResponseBase<List<Long>> rolePermissionIds(@PathVariable("id") Long id) {
        return ResponseBase.ok(roleService.getRolePermissionIds(id));
    }

    @PutMapping("/{id}/permission")
    @PreAuthorize("@auth.check('sys:role:permission:modify')")
    public ResponseBase<Void> modifyRolePermissions(
            @PathVariable("id") Long id, @RequestBody @Valid ModifyRolePermissionsForm form) {
        roleService.modifyRolePermissions(id, form.getPermissionIds());
        return ResponseBase.ok();
    }

    @GetMapping("/{id}/menu")
    @PreAuthorize("@auth.check('sys:role:menu:read')")
    public ResponseBase<List<Long>> getRoleMenuIds(@PathVariable("id") Long id) {
        return ResponseBase.ok(roleService.getRoleMenuIds(id));
    }

    @PutMapping("/{id}/menu")
    @PreAuthorize("@auth.check('sys:role:menu:modify')")
    public ResponseBase<Void> modifyRoleMenus(
            @PathVariable("id") Long id, @RequestBody @Valid ModifyRoleMenusForm form) {
        roleService.modifyRoleMenus(id, form.getMenuIds());
        return ResponseBase.ok();
    }

    @GetMapping("/{id}/menu/tree")
    @PreAuthorize("@auth.check('sys:role:menu:read')")
    public ResponseBase<List<MenuTreeDTO>> getRoleMenuTree(@PathVariable("id") Long id) {
        return ResponseBase.ok(roleService.getRoleMenuTree(id));
    }
}
