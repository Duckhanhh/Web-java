package org.example.abcbook.mapper;

import org.example.abcbook.dto.request.UserRequest;
import org.example.abcbook.dto.response.UserResponse;
import org.example.abcbook.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users userRequestToUser(UserRequest userRequest);

    UserResponse toUserResponse(Users user);
}
