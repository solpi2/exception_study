package com.example.exception_study.controller;

import com.example.exception_study.dto.UserRequest;
import com.example.exception_study.dto.UserResponse;
import com.example.exception_study.exception.BusinessException;
import com.example.exception_study.exception.DuplicateException;
import com.example.exception_study.exception.ValidationException;
import com.example.exception_study.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/web/users")
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;

    @GetMapping
    public String userList(Model model) {
        log.info("사용자 목록 페이지 요청");
        List<UserResponse> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        log.info("사용자 상세 페이지 요청: {}", id);
        UserResponse user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/detail";
    }

    @GetMapping("/new")
    public String userForm(Model model) {
        log.info("사용자 생성 폼 페이지 요청");
        model.addAttribute("userRequest", new UserRequest());
        return "user/form";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute UserRequest userRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("사용자 생성 요청: {}", userRequest.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("사용자 생성 폼 검증 실패: {} errors", bindingResult.getErrorCount());
            return "user/form";
        }

        try {
            UserResponse user = userService.createUser(userRequest);
            redirectAttributes.addFlashAttribute("successMessage",
                    "사용자가 성공적으로 생성되었습니다: " + user.getUsername());
            return "redirect:/web/users";
        } catch (ValidationException e) {
            log.warn("사용자 생성 검증 실패: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            if (e.getFieldErrors() != null && !e.getFieldErrors().isEmpty()) {
                model.addAttribute("fieldErrors", e.getFieldErrors());
            }
            return "user/form";
        } catch (DuplicateException e) {
            log.warn("사용자 생성 중복 오류: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/form";
        } catch (BusinessException e) {
            log.warn("사용자 생성 비즈니스 오류: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/form";
        } catch (Exception e) {
            log.error("사용자 생성 중 예상치 못한 오류 발생", e);
            throw e;
        }
    }

    @GetMapping("/{id}/edit")
    public String userEditForm(@PathVariable Long id, Model model) {
        log.info("사용자 수정 폼 페이지 요청: {}", id);
        UserResponse user = userService.getUserById(id);

        UserRequest userRequest = UserRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        model.addAttribute("userRequest", userRequest);
        model.addAttribute("userId", id);
        return "user/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute UserRequest userRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("사용자 수정 요청: {}", id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            return "user/edit";
        }

        try {
            UserResponse user = userService.updateUser(id, userRequest);
            redirectAttributes.addFlashAttribute("successMessage",
                    "사용자 정보가 성공적으로 수정되었습니다: " + user.getUsername());
            return "redirect:/web/users/" + id;
        } catch (DuplicateException e) {
            log.warn("사용자 수정 중복 오류: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userId", id);
            return "user/edit";
        } catch (BusinessException e) {
            log.warn("사용자 수정 비즈니스 오류: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userId", id);
            return "user/edit";
        } catch (Exception e) {
            log.error("사용자 수정 중 예상치 못한 오류 발생", e);
            throw e;
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("사용자 삭제 요청: {}", id);

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "사용자가 성공적으로 삭제되었습니다");
        return "redirect:/web/users";
    }
}