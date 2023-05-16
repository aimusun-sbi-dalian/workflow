package com.sbi.expo.bo.base.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StaffDTO
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Long id;
    /** username */
    private String name;
    /** 0-disable,1-enable */
    private Integer status;

    private Long roleId;
    private String roleName;
    /** staff version */
    private Integer version;
}
