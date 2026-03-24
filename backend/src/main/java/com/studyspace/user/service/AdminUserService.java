package com.studyspace.user.service;

import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.user.dto.response.UserDetailResponse;
import com.studyspace.user.dto.response.UserSummaryResponse;
import com.studyspace.user.entity.User;
import com.studyspace.user.mapper.UserMapper;
import com.studyspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(userMapper::toUserSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toUserDetailResponse(user);
    }

    @Transactional
    public UserDetailResponse updateUserStatus(Long userId, Boolean active) {
        if (active == null) {
            throw new BadRequestException("Active status is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(active);

        User savedUser = userRepository.save(user);
        return userMapper.toUserDetailResponse(savedUser);
    }
}