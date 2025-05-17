package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.request.UserRequest;
import org.bpm.abcbook.dto.response.UserResponse;
import org.bpm.abcbook.model.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    Staff userRequestToUser(UserRequest userRequest);

    UserResponse toUserResponse(Staff user);
}
