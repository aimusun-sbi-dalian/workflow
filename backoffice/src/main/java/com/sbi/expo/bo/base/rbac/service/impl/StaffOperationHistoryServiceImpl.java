package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.rbac.dto.AuthStaffDTO;
import com.sbi.expo.bo.base.rbac.model.StaffOperationHistory;
import com.sbi.expo.bo.base.rbac.repository.StaffOperationHistoryRepository;
import com.sbi.expo.bo.base.rbac.service.StaffOperationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StaffOperationHistoryServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Service
@Transactional(readOnly = true)
public class StaffOperationHistoryServiceImpl implements StaffOperationHistoryService {

    @Autowired private StaffOperationHistoryRepository staffOperationHistoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void history(String operation) {
        AuthStaffDTO user =
                (AuthStaffDTO)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyById(user.getId(), user.getUsername(), operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void historyById(long staffId, String staffName, String operation) {
        // operation history
        StaffOperationHistory history =
                StaffOperationHistory.builder()
                        .staffId(staffId)
                        .staffName(staffName)
                        .operation(operation)
                        .createdAt(System.currentTimeMillis())
                        .build();
        staffOperationHistoryRepository.save(history);
    }
}
