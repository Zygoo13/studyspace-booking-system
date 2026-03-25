package com.studyspace.booking.mapper;

import com.studyspace.booking.dto.response.BookingResponse;
import com.studyspace.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "customerUserId", source = "customerUser.id")
    @Mapping(target = "spaceUnitId", source = "spaceUnit.id")
    @Mapping(target = "spaceName", source = "spaceUnit.name")
    @Mapping(target = "spaceType", source = "spaceUnit.spaceType")
    @Mapping(target = "comboPlanId", source = "comboPlan.id")
    @Mapping(target = "comboPlanName", source = "comboPlan.name")
    BookingResponse toResponse(Booking booking);
}