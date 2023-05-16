package com.sbi.expo.bo.base.rbac.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.rbac.dto.PermissionsTreeDTO;
import com.sbi.expo.bo.base.rbac.form.AddPermissionForm;
import com.sbi.expo.bo.base.rbac.form.ModifyPermissionForm;
import com.sbi.expo.bo.base.rbac.service.PermissionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * PermissionController
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@RestController
@RequestMapping("/int/v1/permission")
public class PermissionController {

    @Autowired private PermissionService permissionService;

    @GetMapping("/tree")
    @PreAuthorize("@auth.check('sys:permission:read')")
    public ResponseBase<List<PermissionsTreeDTO>> permissionsTree() {
        return ResponseBase.ok(permissionService.permissionsTree());
    }

    @PostMapping("")
    @PreAuthorize("@auth.check('sys:permission:create')")
    public ResponseBase<Void> create(@RequestBody @Valid AddPermissionForm form) {
        permissionService.create(form);
        return ResponseBase.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@auth.check('sys:permission:modify')")
    public ResponseBase<Void> modify(
            @PathVariable("id") Long permissionId, @RequestBody @Valid ModifyPermissionForm form) {
        permissionService.modify(permissionId, form);
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.check('sys:permission:delete')")
    public ResponseBase<Void> delete(@PathVariable("id") Long permissionId) {
        permissionService.delete(permissionId);
        return ResponseBase.ok();
    }
}
