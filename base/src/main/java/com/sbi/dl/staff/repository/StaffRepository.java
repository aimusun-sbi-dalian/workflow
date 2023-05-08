package com.sbi.dl.staff.repository;

import com.sbi.dl.staff.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * StaffRepository
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffRepository
        extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {

    Optional<Staff> findByDeletedFalseAndUsername(String username);

    Optional<Staff> findByIdAndVersion(Long id, Integer version);
}
