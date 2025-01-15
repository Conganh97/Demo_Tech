package com.demo.mapstruct;

import com.demo.mapstruct.dto.UserDto;
import com.demo.mapstruct.entity.UserEntity;
import com.demo.mapstruct.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        // Sử dụng mapper để chuyển đổi Entity -> DTO
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto userDTO = userMapper.entityToDto(userEntity);

        log.info("UserDTO:");
        log.info("Name: {}", userDTO.getName());
        log.info("Email: {}", userDTO.getEmail());

        // Chuyển đổi ngược DTO -> Entity
        UserDto newUserDTO = new UserDto();
        newUserDTO.setName("Jane Doe");
        newUserDTO.setEmail("jane.doe@example.com");

        UserEntity newUserEntity = userMapper.dtoToEntity(newUserDTO);
        log.info("UserEntity:");
        log.info("Name: {}", newUserEntity.getName());
        log.info("Email: {}", newUserEntity.getEmail());
    }
}
