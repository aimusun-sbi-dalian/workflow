package com.sbi.expo.bo.base.rbac.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * MenuTreeDTO
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
@Builder
public class MenuTreeDTO {
    private Long id;
    private String name;
    private String key;
    private String routing;
    private List<MenuTreeDTO> children;
}
