package com.sbi.expo.bo.base.rbac.mapstruct;

import com.sbi.expo.bo.base.rbac.dto.StaffDTO;
import com.sbi.expo.bo.base.rbac.model.Staff;
import com.sbi.expo.commons.BaseMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper extends BaseMapper<StaffDTO, Staff> {

    @Override
    @Mappings({
        @Mapping(source = "username", target = "name"),
        @Mapping(source = "role.id", target = "roleId"),
        @Mapping(source = "role.name", target = "roleName")
    })
    StaffDTO toDTO(Staff staff);

    @Override
    @Mappings({
        @Mapping(source = "username", target = "name"),
        @Mapping(source = "role.id", target = "roleId"),
        @Mapping(source = "role.name", target = "roleName")
    })
    List<StaffDTO> toDTO(List<Staff> staffs);
}
