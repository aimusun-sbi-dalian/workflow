package com.sbi.expo.bo.base.rbac.repository;

import com.sbi.expo.bo.base.rbac.model.Staff;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * StaffRepository
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffRepository
        extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {

    Optional<Staff> findByDeletedFalseAndUsername(String username);

    List<Staff> findAllByRoleIdAndDeletedIsFalse(Long roleId);
}
