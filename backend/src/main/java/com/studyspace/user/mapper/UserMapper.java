package com.studyspace.user.mapper;

import com.studyspace.user.dto.response.AuthUserResponse;
import com.studyspace.user.dto.response.UserDetailResponse;
import com.studyspace.user.dto.response.UserSummaryResponse;
import com.studyspace.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "active", source = "isActive")
    AuthUserResponse toAuthUserResponse(User user);

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "active", source = "isActive")
    UserSummaryResponse toUserSummaryResponse(User user);

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "active", source = "isActive")
    UserDetailResponse toUserDetailResponse(User user);
}