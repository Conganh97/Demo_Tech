package com.demo.mapstruct.mapper;

import com.demo.mapstruct.dto.UserDto;
import com.demo.mapstruct.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Ánh xạ từ Entity sang DTO
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserDto entityToDto(UserEntity userEntity);

    // Ánh xạ từ DTO sang Entity
    @Mapping(target = "id", ignore = true) // Bỏ qua id khi mapping ngược
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserEntity dtoToEntity(UserDto userDTO);
}
