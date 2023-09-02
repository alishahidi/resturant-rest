package com.neshan.resturantrest.mapper;

import com.neshan.resturantrest.dto.AuthenticationDto;
import com.neshan.resturantrest.dto.UserDto;
import com.neshan.resturantrest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AuthenticationMapper {
    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    AuthenticationDto authenticationToAuthenticationDto(User user, String token);

    default UserDto userToUserDto(User user) {
        return Mappers.getMapper(UserMapper.class).userToUserDTO(user);
    }
}
