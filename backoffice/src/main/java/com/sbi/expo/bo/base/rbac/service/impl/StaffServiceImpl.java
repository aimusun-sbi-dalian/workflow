package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.rbac.dto.AuthStaffDTO;
import com.sbi.expo.bo.base.rbac.dto.StaffDTO;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffForm;
import com.sbi.expo.bo.base.rbac.form.ModifyStaffPasswordForm;
import com.sbi.expo.bo.base.rbac.mapstruct.StaffMapper;
import com.sbi.expo.bo.base.rbac.model.Role;
import com.sbi.expo.bo.base.rbac.model.Staff;
import com.sbi.expo.bo.base.rbac.repository.StaffRepository;
import com.sbi.expo.bo.base.rbac.service.RoleService;
import com.sbi.expo.bo.base.rbac.service.StaffOperationHistoryService;
import com.sbi.expo.bo.base.rbac.service.StaffService;
import com.sbi.expo.bo.base.rbac.type.StaffStatus;
import com.sbi.expo.commons.CommonConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StaffServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class StaffServiceImpl implements StaffService {

    @Autowired private StaffMapper staffMapper;
    @Autowired private RoleService roleService;
    @Autowired private StaffRepository staffRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private StaffOperationHistoryService staffOperationHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStaff(String username, Long roleId) {
        // verify roleId
        Role role = roleService.getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        // verify username
        final Optional<Staff> old = getStaffByUsername(username);
        if (old.isPresent()) {
            throw new BizPromptException(MessageConstant.CODE_10202);
        }
        Staff staff =
                Staff.builder()
                        .role(role)
                        .username(username)
                        .password(bCryptPasswordEncoder.encode(username))
                        .status(StaffStatus.ENABLE.getValue())
                        .build();
        staffRepository.save(staff);
        String operation =
                String.format(
                        "Create staff(%s) successfully, role: %s",
                        staff.getUsername(), role.getName());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    private Optional<Staff> getStaffByUsername(String username) {
        return staffRepository.findByDeletedFalseAndUsername(username);
    }

    @Override
    public AuthStaffDTO getAuthStaffByUsername(String username) {
        Optional<Staff> opt = getStaffByUsername(username);
        if (opt.isEmpty()) {
            return null;
        }
        Staff staff = opt.get();
        Role role = staff.getRole();
        List<String> permissions = role.getPermissionKeys();
        return new AuthStaffDTO(
                staff.getId(),
                staff.getUsername(),
                staff.getPassword(),
                staff.getStatus(),
                role.getId(),
                permissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStaff(Long staffId) {
        Staff staff =
                getStaffById(staffId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10204));
        staff.delete();
        staff.setRole(null);
        String operation =
                String.format(
                        "Delete staff successfully, id:%s, username:%s",
                        staff.getId(), staff.getUsername());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    private Optional<Staff> getStaffById(Long staffId) {
        return staffRepository.findById(staffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPassword(AuthStaffDTO authStaff, ModifyStaffPasswordForm form) {
        if (!bCryptPasswordEncoder.matches(form.getOldPassword(), authStaff.getPassword())) {
            log.info("Modify staff password failed, old password is wrong");
            throw new BizPromptException(MessageConstant.CODE_10205);
        }
        Staff staff =
                getStaffById(authStaff.getId())
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10204));
        staff.setPassword(bCryptPasswordEncoder.encode(form.getNewPassword()));
        String operation = "Modify staff password successfully";
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    public StaffDTO getStaff(String idOrName) {
        if (StringUtils.isBlank(idOrName)) {
            throw new BizPromptException(MessageConstant.CODE_10204);
        }
        Staff staff =
                StringUtils.isNumeric(idOrName)
                        ? getStaffById(Long.valueOf(idOrName))
                                .orElseThrow(
                                        () -> new BizPromptException(MessageConstant.CODE_10204))
                        : getStaffByUsername(idOrName)
                                .orElseThrow(
                                        () -> new BizPromptException(MessageConstant.CODE_10204));
        return staffMapper.toDTO(staff);
    }

    @Override
    public Page<StaffDTO> getStaffList(Pageable pageable) {
        Specification<Staff> spec =
                Specification.where(
                        (root, query, cb) -> cb.equal(root.get(CommonConstant.DELETED), false));
        Page<Staff> staffPage =
                staffRepository.findAll(
                        spec,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Direction.DESC, CommonConstant.ID)));
        List<Staff> staffs = staffPage.getContent();
        List<StaffDTO> result = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(staffs)) {
            result = staffMapper.toDTO(staffs);
        }
        return new PageImpl<>(result, pageable, staffPage.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyStaff(Long staffId, ModifyStaffForm form) {
        Staff staff =
                getStaffById(staffId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10204));
        if (!Objects.equals(staff.getVersion(), form.getVersion())) {
            throw new BizPromptException(MessageConstant.CODE_10206);
        }
        Role role = roleService.getRole(form.getRoleId());
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        staff.setStatus(form.getStatus());
        staff.setVersion(form.getVersion());
        staff.setRole(role);
        String operation =
                String.format(
                        "Modify staff(id:%s) successfully, status:%s, role:%s",
                        staffId, form.getStatus(), role.getName());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long staffId) {
        Staff staff =
                getStaffById(staffId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10204));
        staff.setPassword(bCryptPasswordEncoder.encode(staff.getUsername()));
        String operation = String.format("Reset staff(id:%s) password successfully", staffId);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }
}
