package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.form.MenuForm;
import com.sbi.expo.bo.base.rbac.model.Menu;
import com.sbi.expo.bo.base.rbac.model.Role;
import com.sbi.expo.bo.base.rbac.repository.MenuRepository;
import com.sbi.expo.bo.base.rbac.service.MenuService;
import com.sbi.expo.bo.base.rbac.service.StaffOperationHistoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MenuServiceImpl
 *
 * @author Ming.G
 * @date 2022-07-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    @Autowired private MenuRepository menuRepository;
    @Autowired private StaffOperationHistoryService staffOperationHistoryService;

    @Override
    public List<MenuTreeDTO> getMenuTree() {
        // get all super parent menus
        List<Menu> parentMenus =
                menuRepository.findByDeletedFalseAndVisibleTrueAndPidOrderBySortAsc(0L);
        if (CollectionUtils.isEmpty(parentMenus)) {
            return new ArrayList<>(0);
        }
        return recursiveMenus(parentMenus, new ArrayList<>(0));
    }

    @Override
    public List<MenuTreeDTO> getMenuTree(Role role) {
        // get all super parent menus
        List<Menu> parentMenus =
                role.getMenus().stream()
                        .filter(menu -> 0L == menu.getPid())
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parentMenus)) {
            return new ArrayList<>(0);
        }
        return recursiveMenus(parentMenus, role.getMenusId());
    }

    private List<MenuTreeDTO> recursiveMenus(List<Menu> parentMenus, List<Long> roleMenuIds) {
        List<MenuTreeDTO> list = new ArrayList<>(parentMenus.size());
        parentMenus.forEach(
                m -> {
                    // exclude visible is false and repeated
                    if (BooleanUtils.isNotTrue(m.getVisible())
                            || (CollectionUtils.isNotEmpty(roleMenuIds)
                                    && !roleMenuIds.contains(m.getId()))) {
                        return;
                    }
                    List<MenuTreeDTO> children = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(m.getChildren())) {
                        children = recursiveMenus(m.getChildren(), roleMenuIds);
                    }
                    list.add(
                            MenuTreeDTO.builder()
                                    .id(m.getId())
                                    .name(m.getName())
                                    .children(children)
                                    .routing(m.getRouting())
                                    .key(m.getMenuKey())
                                    .build());
                });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MenuForm form) {
        // verify parent menu
        Long pid = form.getPid();
        if (0 != pid && !menuRepository.existsById(pid)) {
            throw new BizPromptException(MessageConstant.CODE_10219);
        }
        Menu menu =
                Menu.builder()
                        .pid(pid)
                        .name(form.getName())
                        .sort(form.getSort())
                        .routing(form.getRouting())
                        .visible(form.getVisible())
                        .build();

        menuRepository.save(menu);
        String operation = String.format("Create Menu(%s) successfully", menu.getName());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long menuId, MenuForm form) {
        // verify menu
        Menu menu =
                menuRepository
                        .findById(menuId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10220));
        // verify parent menu
        Long pid = form.getPid();
        if (0 != pid && !menuRepository.existsById(pid)) {
            throw new BizPromptException(MessageConstant.CODE_10219);
        }
        // modify menu
        menu.setPid(form.getPid());
        menu.setName(form.getName());
        menu.setSort(form.getSort());
        menu.setRouting(form.getRouting());
        menu.setVisible(form.getVisible());
        String operation =
                String.format(
                        "Modify menu(id:%s) successfully,"
                                + " name:'%s',pid:%s,sort:%s,routing:%s,visible:%s",
                        menuId,
                        form.getName(),
                        form.getPid(),
                        form.getSort(),
                        form.getRouting(),
                        form.getVisible());
        menuRepository.save(menu);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId) {
        // verify menu
        Menu menu =
                menuRepository
                        .findById(menuId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10220));
        if (CollectionUtils.isNotEmpty(menu.getChildren())) {
            throw new BizPromptException(MessageConstant.CODE_10221);
        }
        menu.setDeleted(true);
        String operation =
                String.format("Delete menu successfully, id:%s, name:'%s'", menuId, menu.getName());
        menuRepository.save(menu);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    public List<Long> getAllMenuIds() {
        return menuRepository.findByDeletedFalseAndVisibleTrue().stream().map(Menu::getId).toList();
    }

    @Override
    public List<Menu> getMenus(List<Long> menuIds) {
        return menuRepository.findByDeletedFalseAndIdIn(menuIds);
    }
}
