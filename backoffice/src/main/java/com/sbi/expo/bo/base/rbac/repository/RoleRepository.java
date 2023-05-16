package com.sbi.expo.bo.base.rbac.repository;

import com.sbi.expo.bo.base.rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * RoleRepository
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findByName(String roleName);

    Role findByIdAndDeletedFalse(Long roleId);

    Role findByNameAndDeletedIsFalse(String adminRoleName);
}
