package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.rbac.dto.AuthStaffDTO;
import com.sbi.expo.bo.base.rbac.service.AuthService;
import com.sbi.expo.bo.base.rbac.service.RoleService;
import com.sbi.expo.bo.base.rbac.service.StaffService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ming.G
 */
@Slf4j
@Service("auth")
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    @Autowired private RoleService roleService;
    @Autowired private StaffService staffService;

    @Override
    public UserDetails loadUserByUsername(String name) {
        return staffService.getAuthStaffByUsername(name);
    }

    public boolean check(String... permissions) {
        AuthStaffDTO authStaff =
                (AuthStaffDTO)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (roleService.isAdmin(authStaff.getRoleId())) {
            return true;
        }
        return Arrays.stream(permissions).anyMatch(authStaff.getPermissions()::contains);
    }
}
