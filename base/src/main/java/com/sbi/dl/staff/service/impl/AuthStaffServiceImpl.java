package com.sbi.dl.staff.service.impl;


import com.sbi.dl.auth.service.AuthService;
import com.sbi.dl.staff.model.Staff;
import com.sbi.dl.staff.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthStaffServiceImpl implements  AuthService {

    @Autowired private StaffService staffService;

    @Override
    public Object loginFailedResponseData(AuthenticationException exception) {
        return null;
    }

    @Override
    public Object jwtAuthFailedResponseData(AuthenticationException exception) {
        return null;
    }

    @Override
    public Object unauthorizedResponseData(AuthenticationException exception) {
        return null;
    }

    @Override
    public Staff loadUserByUsername(String username) throws UsernameNotFoundException {
        return staffService.getStaffByUsername(username).get();
    }

}
