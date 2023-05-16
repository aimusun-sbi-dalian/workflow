package com.sbi.expo.bo.base.rbac.repository;

import com.sbi.expo.bo.base.rbac.model.Permission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * PermissionRepository
 *
 * @author Ming.G
 * @date 2022-05-25
 */
public interface PermissionRepository
        extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * get permission list by parent id
     *
     * @param parentId parentId
     * @return List<Permission>
     */
    List<Permission> findByDeletedFalseAndPid(Long parentId);

    /**
     * get all permission list
     *
     * @return List<Permission>
     */
    List<Permission> findByDeletedFalseOrderByCreatedAtAsc();

    /**
     * get permissions
     *
     * @param permissionIds permissionIds
     * @return List<Permission>
     */
    List<Permission> findByDeletedFalseAndIdIn(List<Long> permissionIds);

    /**
     * get permission by permission key
     *
     * @param permission permission key
     * @return Optional<Permission>
     */
    Optional<Permission> findFirstByDeletedFalseAndPermission(String permission);

    /**
     * get permission by id
     *
     * @param permissionId permissionId
     * @return Optional<Permission>
     */
    Optional<Permission> findFirstByDeletedFalseAndId(Long permissionId);
}
