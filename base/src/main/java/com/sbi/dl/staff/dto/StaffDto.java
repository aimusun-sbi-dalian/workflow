package com.sbi.dl.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StaffDto
 *
 * @author Ming.G
 * @date 2022-05-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Long id;
    /** username */
    private String name;
    /** 0-disable,1-enable */
    private Integer status;

    /** staff version */
    private Integer version;
}
