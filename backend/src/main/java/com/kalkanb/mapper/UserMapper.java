package com.kalkanb.mapper;

import com.kalkanb.dto.UserDto;
import com.kalkanb.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserDto userDto);

    void toEntity(@MappingTarget UserEntity userEntity, UserDto userDto);
}
