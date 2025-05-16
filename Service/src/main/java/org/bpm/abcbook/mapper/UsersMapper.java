package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.request.UserRequest;
import org.bpm.abcbook.dto.response.UserResponse;
import org.bpm.abcbook.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Users userRequestToUser(UserRequest userRequest);

    UserResponse toUserResponse(Users user);
}
