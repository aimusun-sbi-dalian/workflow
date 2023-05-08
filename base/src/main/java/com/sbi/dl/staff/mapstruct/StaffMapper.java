package com.sbi.dl.staff.mapstruct;


import com.sbi.dl.compoment.BaseMapper;
import com.sbi.dl.staff.dto.StaffDto;
import com.sbi.dl.staff.model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper extends BaseMapper<StaffDto, Staff> {

    @Override
    @Mappings({@Mapping(source = "username", target = "name")})
    StaffDto toDTO(Staff staff);

    @Override
    @Mappings({@Mapping(source = "username", target = "name")})
    List<StaffDto> toDTO(List<Staff> staffs);
}
