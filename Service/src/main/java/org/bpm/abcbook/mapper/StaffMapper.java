package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.request.StaffRequest;
import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.model.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff staffRequestToStaff(StaffRequest staffRequest);

    StaffResponse toUserResponse(Staff user);
}
