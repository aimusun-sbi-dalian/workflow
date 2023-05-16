package com.sbi.expo.bo.base.rbac.repository;

import com.sbi.expo.bo.base.rbac.model.StaffOperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * StaffOperationHistoryRepository
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffOperationHistoryRepository
        extends JpaRepository<StaffOperationHistory, Long>,
                JpaSpecificationExecutor<StaffOperationHistory> {}
