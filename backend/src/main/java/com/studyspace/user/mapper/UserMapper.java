package com.studyspace.user.mapper;

import com.studyspace.auth.dto.response.AuthUserResponse;
import com.studyspace.user.dto.response.UserResponse;
import com.studyspace.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    UserResponse toResponse(User user);

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "active", source = "isActive")
    AuthUserResponse toAuthUserResponse(User user);
}