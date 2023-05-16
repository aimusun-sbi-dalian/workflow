package com.sbi.expo.bo.base.rbac.service.impl;

import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.rbac.dto.PermissionsTreeDTO;
import com.sbi.expo.bo.base.rbac.form.AddPermissionForm;
import com.sbi.expo.bo.base.rbac.form.ModifyPermissionForm;
import com.sbi.expo.bo.base.rbac.model.Permission;
import com.sbi.expo.bo.base.rbac.repository.PermissionRepository;
import com.sbi.expo.bo.base.rbac.service.PermissionService;
import com.sbi.expo.bo.base.rbac.service.StaffOperationHistoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PermissionServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    @Autowired private PermissionRepository permissionRepository;
    @Autowired private StaffOperationHistoryService staffOperationHistoryService;

    @Override
    public List<PermissionsTreeDTO> permissionsTree() {
        // get all super parent permissions
        List<Permission> parentPermissions = permissionRepository.findByDeletedFalseAndPid(0L);
        if (CollectionUtils.isEmpty(parentPermissions)) {
            return new ArrayList<>(0);
        }
        return recursivePermissions(parentPermissions);
    }

    @Override
    public List<Long> getAllPermissionIds() {
        return permissionRepository.findByDeletedFalseOrderByCreatedAtAsc().stream()
                .map(Permission::getId)
                .toList();
    }

    private List<PermissionsTreeDTO> recursivePermissions(List<Permission> parentPermissions) {
        List<PermissionsTreeDTO> list = new ArrayList<>(parentPermissions.size());
        parentPermissions.forEach(
                p -> {
                    List<PermissionsTreeDTO> children = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(p.getChildren())) {
                        children = recursivePermissions(p.getChildren());
                    }
                    list.add(
                            PermissionsTreeDTO.builder()
                                    .id(p.getId())
                                    .key(p.getPermission())
                                    .name(p.getName())
                                    .children(children)
                                    .build());
                });
        return list;
    }

    @Override
    public List<Permission> getPermissions(List<Long> permissionIds) {
        return permissionRepository.findByDeletedFalseAndIdIn(permissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AddPermissionForm form) {
        // verify permission exist
        getPermission(form.getKey())
                .orElseThrow(
                        () ->
                                new BizPromptException(
                                        0 == form.getType()
                                                ? MessageConstant.CODE_10213
                                                : MessageConstant.CODE_10214));
        // verify parent permission
        verifyPermissionGroup(form.getPid());
        Permission permission =
                Permission.builder()
                        .permission(form.getKey())
                        .name(form.getName())
                        .parentId(form.getPid())
                        .type(form.getType())
                        .build();
        permissionRepository.save(permission);
        String operation =
                String.format("Create Permission(%s) successfully", permission.getPermission());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long permissionId, ModifyPermissionForm form) {
        // verify permission
        Permission permission =
                getPermission(permissionId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10216));
        if (!permission.getVersion().equals(form.getVersion())) {
            throw new BizPromptException(MessageConstant.CODE_10217);
        }
        // verify parent permission
        verifyPermissionGroup(form.getPid());
        permission.setPid(form.getPid());
        permission.setPermission(form.getKey());
        permission.setName(form.getName());
        permission.setType(form.getType());
        permission.setVersion(form.getVersion());
        String operation =
                String.format(
                        "Modify Permission(id:%s) successfully, name:'%s',pid:%s,type:%s",
                        permissionId, form.getName(), form.getPid(), form.getType());
        permissionRepository.save(permission);
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long permissionId) {
        Permission permission =
                getPermission(permissionId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10216));
        if (1 == permission.getType() && CollectionUtils.isNotEmpty(permission.getChildren())) {
            throw new BizPromptException(MessageConstant.CODE_10218);
        }
        permission.setDeleted(true);
        String operation =
                String.format(
                        "Delete permission successfully, id:%s, permission key:'%s'",
                        permissionId, permission.getPermission());
        staffOperationHistoryService.history(operation);
        log.info(operation);
    }

    /**
     * verify permission group
     *
     * @param parentId permission id
     */
    private void verifyPermissionGroup(Long parentId) {
        if (0 == parentId) {
            return;
        }
        Permission parent =
                getPermission(parentId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10215));
        if (1 != parent.getType()) {
            throw new BizPromptException(MessageConstant.CODE_10215);
        }
    }

    /**
     * get permission by permission key
     *
     * @param permission permission key
     * @return Optional<Permission>
     */
    private Optional<Permission> getPermission(String permission) {
        return permissionRepository.findFirstByDeletedFalseAndPermission(permission);
    }

    /**
     * get permission by permission id
     *
     * @param permissionId permissionId
     * @return Optional<Permission>
     */
    private Optional<Permission> getPermission(Long permissionId) {
        return permissionRepository.findFirstByDeletedFalseAndId(permissionId);
    }
}
