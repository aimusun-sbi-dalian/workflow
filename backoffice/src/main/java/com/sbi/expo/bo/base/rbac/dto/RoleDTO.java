package com.sbi.expo.bo.base.rbac.dto;

import com.sbi.expo.bo.base.rbac.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RoleDTO
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long roleId;
    private String roleName;
    private Integer version;

    public static RoleDTO newInstance(Role role) {
        return RoleDTO.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .version(role.getVersion())
                .build();
    }
}
