package com.example.compoint.mappers;

import com.example.compoint.dtos.RoleResponse;
import com.example.compoint.dtos.RoleRequest;
import com.example.compoint.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleResponse roleEntityToRoleResponse(RoleEntity roleEntity);
    RoleEntity roleRequestToRoleEntity(RoleRequest roleRequest);
}
