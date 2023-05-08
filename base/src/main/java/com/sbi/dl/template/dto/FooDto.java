package com.sbi.dl.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FooDto {
    private Long id;
    /** username */
    private String name;
    /** 0-disable,1-enable */
    private Integer status;

    /** Foo version */
    private Integer version;
}
