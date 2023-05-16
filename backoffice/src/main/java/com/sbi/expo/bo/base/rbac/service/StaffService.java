package com.sbi.expo.bo.base.rbac.service;

import com.sbi.expo.bo.base.rbac.dto.AuthStaffDTO;
import com.sbi.expo.bo.base.rbac.dto.StaffDTO;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffForm;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffPasswordForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * StaffService
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffService {
    void createStaff(String username, Long roleId);

    AuthStaffDTO getAuthStaffByUsername(String username);

    void deleteStaff(Long staffId);

    void modifyPassword(AuthStaffDTO authStaff, ModifyStaffPasswordForm form);

    StaffDTO getStaff(String idOrName);

    Page<StaffDTO> getStaffList(Pageable pageable);

    void modifyStaff(Long staffId, ModifyStaffForm form);

    void resetPassword(Long staffId);
}
