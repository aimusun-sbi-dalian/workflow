package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.constant.AuthConstant;
import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.rbac.dto.MenuTreeDTO;
import com.sbi.expo.bo.base.rbac.dto.RoleDTO;
import com.sbi.expo.bo.base.rbac.model.Menu;
import com.sbi.expo.bo.base.rbac.model.Permission;
import com.sbi.expo.bo.base.rbac.model.Role;
import com.sbi.expo.bo.base.rbac.repository.RoleRepository;
import com.sbi.expo.bo.base.rbac.service.MenuService;
import com.sbi.expo.bo.base.rbac.service.PermissionService;
import com.sbi.expo.bo.base.rbac.service.RoleService;
import com.sbi.expo.bo.base.rbac.service.StaffOperationHistoryService;
import com.sbi.expo.commons.CommonConstant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RoleServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired private MenuService menuService;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PermissionService permissionService;
    @Autowired private StaffOperationHistoryService staffOperationHistoryService;

    private Long adminRoleId;

    @Override
    public Role getRole(Long roleId) {
        return roleRepository.findByIdAndDeletedFalse(roleId);
    }

    private Role getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(String roleName) {
        Role oldRole = getRole(roleName);
        if (null != oldRole) {
            throw new BizPromptException(MessageConstant.CODE_10207);
        }
        Role role = Role.builder().name(roleName).build();
        roleRepository.save(role);
        String operation =
                String.format("Create Role(%s,id:%s) successfully", role.getName(), role.getId());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRole(Long roleId, String roleName, Integer version) {
        if (isAdmin(roleId)) {
            throw new BizPromptException(MessageConstant.CODE_10212);
        }
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        if (!Objects.equals(role.getVersion(), version)) {
            throw new BizPromptException(MessageConstant.CODE_10208);
        }
        role.setName(roleName);
        role.setVersion(version);
        String operation =
                String.format("Modify role(id:%s) successfully, name:%s", roleId, roleName);
        roleRepository.save(role);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        if (CollectionUtils.isNotEmpty(role.getStaffs())) {
            throw new BizPromptException(MessageConstant.CODE_10209);
        }
        role.delete();
        role.setPermissions(null);
        role.setMenus(null);
        String operation =
                String.format(
                        "Delete role successfully, id:%s, name:%s", role.getId(), role.getName());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    public Page<RoleDTO> getRoleListDTO(Pageable pageable) {
        Specification<Role> spec =
                Specification.where(
                        (root, query, cb) -> cb.equal(root.get(CommonConstant.DELETED), false));
        Page<Role> rolePage =
                roleRepository.findAll(
                        spec,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Direction.ASC, CommonConstant.ID)));
        List<Role> roles = rolePage.getContent();
        List<RoleDTO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            result = roles.stream().map(RoleDTO::newInstance).collect(Collectors.toList());
        }
        return new PageImpl<>(result, pageable, rolePage.getTotalElements());
    }

    @Override
    public RoleDTO getRoleDTO(Long roleId) {
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        return RoleDTO.newInstance(role);
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        if (isAdmin(roleId)) {
            return permissionService.getAllPermissionIds();
        }
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        return role.getPermissionsId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRolePermissions(Long roleId, List<Long> permissionIds) {
        if (isAdmin(roleId)) {
            throw new BizPromptException(MessageConstant.CODE_10211);
        }
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }

        if (CollectionUtils.isEmpty(permissionIds)) {
            // delete role all permissions
            role.setPermissions(null);
            log.info("Modify role permission - clear role({}) permission", roleId);
            // operation history
            String operation =
                    String.format(
                            "Modify role permission - {roleId=%s, permissionIds=%s}",
                            roleId, permissionIds);
            roleRepository.save(role);
            staffOperationHistoryService.history(operation);
            return;
        }
        // get permission
        List<Permission> permissions = permissionService.getPermissions(permissionIds);
        // handle parent permission
        Set<Permission> unSubmitPermissions = getUnSubmitPermissions(permissions, permissionIds);

        if (CollectionUtils.isNotEmpty(unSubmitPermissions)) {
            permissions.addAll(unSubmitPermissions);
        }
        // modify role permissions
        role.setPermissions(permissions);
        roleRepository.save(role);
        // operation history
        String operation =
                String.format(
                        "modify role permission - {roleId=%s, permissionIds=%s}",
                        roleId, permissionIds);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    private Set<Permission> getUnSubmitPermissions(
            Collection<Permission> permissions, List<Long> permissionIds) {
        Set<Permission> unSubmitPermissions =
                permissions.stream()
                        .filter(
                                permission ->
                                        Objects.nonNull(permission.getParent())
                                                && !permissionIds.contains(permission.getPid()))
                        .map(Permission::getParent)
                        .collect(Collectors.toSet());
        Set<Permission> superParentPermissions = null;
        if (CollectionUtils.isNotEmpty(unSubmitPermissions)) {
            superParentPermissions = getUnSubmitPermissions(unSubmitPermissions, permissionIds);
        }
        if (CollectionUtils.isNotEmpty(superParentPermissions)) {
            unSubmitPermissions.addAll(superParentPermissions);
        }
        return unSubmitPermissions;
    }

    private Set<Menu> getUnSubmitMenus(Collection<Menu> menus, List<Long> menuIds) {
        Set<Menu> unSubmitMenus =
                menus.stream()
                        .filter(
                                menu ->
                                        Objects.nonNull(menu.getParent())
                                                && !menuIds.contains(menu.getPid()))
                        .map(Menu::getParent)
                        .collect(Collectors.toSet());
        Set<Menu> superParentMenus = null;
        if (CollectionUtils.isNotEmpty(unSubmitMenus)) {
            superParentMenus = getUnSubmitMenus(unSubmitMenus, menuIds);
        }
        if (CollectionUtils.isNotEmpty(superParentMenus)) {
            unSubmitMenus.addAll(superParentMenus);
        }
        return unSubmitMenus;
    }

    private Long getAdminRoleId() {
        if (Objects.isNull(adminRoleId)) {
            Role role = roleRepository.findByNameAndDeletedIsFalse(AuthConstant.ADMIN_ROLE_NAME);
            if (Objects.isNull(role)) {
                throw new BizPromptException(MessageConstant.CODE_10201);
            }
            adminRoleId = role.getId();
        }
        return adminRoleId;
    }

    @Override
    public boolean isAdmin(Long roleId) {
        return getAdminRoleId().equals(roleId);
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        if (isAdmin(roleId)) {
            return menuService.getAllMenuIds();
        }
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }
        return role.getMenusId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleMenus(Long roleId, List<Long> menuIds) {
        if (isAdmin(roleId)) {
            throw new BizPromptException(MessageConstant.CODE_10222);
        }
        Role role = getRole(roleId);
        if (Objects.isNull(role)) {
            throw new BizPromptException(MessageConstant.CODE_10201);
        }

        if (CollectionUtils.isEmpty(menuIds)) {
            // delete role all menus
            role.setMenus(null);
            log.info("Modify role menu - clear role({}) menu", roleId);
            // operation history
            String operation =
                    String.format("Modify role menu - {roleId=%s, menuIds=%s}", roleId, menuIds);
            roleRepository.save(role);
            staffOperationHistoryService.history(operation);
            return;
        }
        // get menu
        List<Menu> menus = menuService.getMenus(menuIds);
        // handle parent menu
        Set<Menu> unSubmitMenus = getUnSubmitMenus(menus, menuIds);

        if (CollectionUtils.isNotEmpty(unSubmitMenus)) {
            menus.addAll(unSubmitMenus);
        }
        // modify role menus
        role.setMenus(menus);
        roleRepository.save(role);
        // operation history
        String operation =
                String.format("modify role menu - {roleId=%s, menuIds=%s}", roleId, menuIds);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    public List<MenuTreeDTO> getRoleMenuTree(Long roleId) {
        return isAdmin(roleId)
                ? menuService.getMenuTree()
                : menuService.getMenuTree(getRole(roleId));
    }
}
