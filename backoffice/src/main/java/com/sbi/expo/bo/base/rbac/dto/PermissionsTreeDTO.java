package com.sbi.expo.bo.base.rbac.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PermissionTreeDTO
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionsTreeDTO {
    private Long id;
    private String key;
    private String name;
    private List<PermissionsTreeDTO> children;
}
