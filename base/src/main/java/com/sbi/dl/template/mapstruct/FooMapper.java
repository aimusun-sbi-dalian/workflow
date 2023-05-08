package com.sbi.dl.template.mapstruct;


import com.sbi.dl.compoment.BaseMapper;
import com.sbi.dl.template.dto.FooDto;
import com.sbi.dl.template.model.Foo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FooMapper extends BaseMapper<FooDto, Foo> {

    @Override
    @Mappings({@Mapping(source = "username", target = "name")})
    FooDto toDTO(Foo Foo);

    @Override
    @Mappings({@Mapping(source = "username", target = "name")})
    List<FooDto> toDTO(List<Foo> Foos);
}
