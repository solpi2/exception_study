package com.example.exception_study.controller;

import com.example.exception_study.dto.UserRequest;
import com.example.exception_study.dto.UserResponse;
import com.example.exception_study.exception.SystemException;
import com.example.exception_study.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /api/users - 모든 사용자 조회");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - 사용자 상세 조회", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("GET /api/users/username/{} - 사용자명으로 조회", username);
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("POST /api/users - 사용자 생성: {}", request.getUsername());
        UserResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {
        log.info("PUT /api/users/{} - 사용자 수정", id);
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - 사용자 삭제", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test/runtime-error")
    public ResponseEntity<String> testRuntimeError() {
        throw new RuntimeException("의도적인 런타임 오류");
    }

    @PostMapping("/test/illegal-argument")
    public ResponseEntity<String> testIllegalArgument() {
        throw new IllegalArgumentException("잘못된 인자 테스트");
    }

    @PostMapping("/test/system-error")
    public ResponseEntity<String> testSystemError() {
        // SystemException을 발생시키기 위한 테스트
        throw new SystemException("시스템 오류 테스트");
    }
}