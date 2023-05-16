package com.sbi.expo.bo.base.rbac.service;

import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.form.MenuForm;
import com.sbi.expo.bo.base.rbac.model.Menu;
import com.sbi.expo.bo.base.rbac.model.Role;
import java.util.List;

/**
 * MenuService
 *
 * @author Ming.G
 * @date 2022-07-28
 */
public interface MenuService {

    /**
     * get all menu tree
     *
     * @return List<MenuTreeDTO>
     */
    List<MenuTreeDTO> getMenuTree();
    /**
     * get menu tree
     *
     * @param role role
     * @return List<MenuTreeDTO>
     */
    List<MenuTreeDTO> getMenuTree(Role role);

    /**
     * create a new menu
     *
     * @param form AddMenuForm
     */
    void create(MenuForm form);

    /**
     * modify menu
     *
     * @param menuId menu id
     * @param form AddMenuForm
     */
    void modify(Long menuId, MenuForm form);

    /**
     * delete menu
     *
     * @param menuId menu id
     */
    void delete(Long menuId);

    /**
     * get all menu ids
     *
     * @return java.util.List<java.lang.Long>
     * @author Ming.G
     * @date 2022-10-11 13:20
     */
    List<Long> getAllMenuIds();

    /**
     * get menu list via menu ids
     *
     * @param menuIds menu ids
     * @return java.util.List<com.sbi.expo.base.rbac.model.Menu>
     * @author Ming.G
     * @date 2022-10-11 13:35
     */
    List<Menu> getMenus(List<Long> menuIds);
}
