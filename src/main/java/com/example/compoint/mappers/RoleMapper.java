package com.example.compoint.mappers;

import com.example.compoint.dtos.RoleDTO;
import com.example.compoint.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO roleEntityToRoleDTO(RoleEntity role);
}
