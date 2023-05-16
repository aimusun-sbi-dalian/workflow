package com.sbi.expo.bo.base.rbac.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.rbac.dto.AuthStaffDTO;
import com.sbi.expo.bo.base.rbac.dto.StaffDTO;
import com.sbi.expo.bo.base.rbac.form.AddStaffForm;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffForm;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffPasswordForm;
import com.sbi.expo.bo.base.rbac.service.StaffService;
import com.sbi.expo.commons.CommonConstant;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * StaffController
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@RestController
@RequestMapping("/int/v1/staff")
public class StaffController {

    @Autowired private StaffService staffService;

    @PostMapping("")
    @PreAuthorize("@auth.check('sys:staff:create')")
    public ResponseBase<Void> createStaff(@RequestBody @Valid AddStaffForm form) {
        // create params
        staffService.createStaff(form.getName(), form.getRoleId());
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.check('sys:staff:delete')")
    public ResponseBase<Void> deleteStaff(
            @PathVariable Long id, @AuthenticationPrincipal AuthStaffDTO authStaff) {
        if (authStaff.getId().equals(id)) {
            throw new BizPromptException(MessageConstant.CODE_10203);
        }
        staffService.deleteStaff(id);
        return ResponseBase.ok();
    }

    @PutMapping("/password")
    public ResponseBase<Void> modifySelfPassword(
            @RequestBody @Valid ModifyStaffPasswordForm form,
            @AuthenticationPrincipal AuthStaffDTO authStaff) {
        staffService.modifyPassword(authStaff, form);
        return ResponseBase.ok();
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("@auth.check('sys:staff:password:modify')")
    public ResponseBase<Void> resetPassword(@PathVariable("id") Long staffId) {
        staffService.resetPassword(staffId);
        return ResponseBase.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@auth.check('sys:staff:modify')")
    public ResponseBase<Void> modifyStaff(
            @RequestBody @Valid ModifyStaffForm form,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthStaffDTO authStaff) {
        if (authStaff.getId().equals(id)) {
            throw new BizPromptException(MessageConstant.CODE_10210);
        }
        staffService.modifyStaff(id, form);
        return ResponseBase.ok();
    }

    @GetMapping("")
    @PreAuthorize("@auth.check('sys:staff:read')")
    public ResponseBase<Page<StaffDTO>> staffList(
            @PageableDefault(sort = {CommonConstant.ID}) Pageable pageable) {
        return ResponseBase.ok(staffService.getStaffList(pageable));
    }

    @GetMapping("/{idOrName}")
    @PreAuthorize("@auth.check('sys:staff:read')")
    public ResponseBase<StaffDTO> staffList(@PathVariable("idOrName") String idOrName) {
        return ResponseBase.ok(staffService.getStaff(idOrName));
    }
}
