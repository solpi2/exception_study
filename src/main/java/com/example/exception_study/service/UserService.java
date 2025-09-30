package com.example.exception_study.service;

import com.example.exception_study.dto.UserRequest;
import com.example.exception_study.dto.UserResponse;
import com.example.exception_study.entity.User;
import com.example.exception_study.exception.DataNotFoundException;
import com.example.exception_study.exception.DuplicateException;
import com.example.exception_study.exception.ValidationException;
import com.example.exception_study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        log.debug("Creating user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateException("사용자명", request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("이메일", request.getEmail());
        }

        validateUserRequest(request);

        User user = request.toEntity();
        User savedUser = userRepository.save(user);

        log.info("User created successfully: {}", savedUser.getUsername());
        return UserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Finding user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("사용자", id));

        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        log.debug("Finding user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("사용자명: " + username));

        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Finding all users");

        return userRepository.findAll()
                .stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        log.debug("Updating user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("사용자", id));

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("이메일", request.getEmail());
        }

        user.updateInfo(request.getEmail(), request.getName());
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully: {}", updatedUser.getUsername());
        return UserResponse.of(updatedUser);
    }

    public void deleteUser(Long id) {
        log.debug("Deleting user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("사용자", id));

        userRepository.delete(user);
        log.info("User deleted successfully: {}", user.getUsername());
    }

    private void validateUserRequest(UserRequest request) {

        if (request.getEmail().endsWith("@blocked.com")) {
            throw new ValidationException("email", request.getEmail(),
                    "해당 도메인의 이메일은 사용할 수 없습니다");
        }

        List<String> forbiddenUsernames = List.of("admin", "root", "system", "test");
        if (forbiddenUsernames.contains(request.getUsername().toLowerCase())) {
            throw new ValidationException("username", request.getUsername(),
                    "사용할 수 없는 사용자명입니다");
        }
    }
}