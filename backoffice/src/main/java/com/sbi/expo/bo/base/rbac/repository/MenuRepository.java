package com.sbi.expo.bo.base.rbac.repository;

import com.sbi.expo.bo.base.rbac.model.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * MenuRepository
 *
 * @author Ming.G
 * @date 2022-05-25
 */
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    /**
     * get menu list via parentId
     *
     * @param parentId parentId
     * @return List<Menu>
     */
    List<Menu> findByDeletedFalseAndVisibleTrueAndPidOrderBySortAsc(Long parentId);

    /**
     * get all menus
     *
     * @return java.util.List<com.sbi.expo.base.rbac.model.Menu>
     * @author Ming.G
     * @date 2022-10-11 13:38
     */
    List<Menu> findByDeletedFalseAndVisibleTrue();

    /**
     * get menu list via menu ids
     *
     * @param menuIds
     * @return java.util.List<com.sbi.expo.base.rbac.model.Menu>
     * @author Ming.G
     * @date 2022-10-11 13:38
     */
    List<Menu> findByDeletedFalseAndIdIn(List<Long> menuIds);
}
