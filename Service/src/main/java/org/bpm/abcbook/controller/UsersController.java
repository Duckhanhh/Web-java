package org.bpm.abcbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bpm.abcbook.dto.request.UserRequest;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.dto.response.UserResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    UsersService usersService;

    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable("id") Long id) {
        return ApiResponse.builder()
                .code(null)
                .success(true)
                .data(usersService.getUserById(id)).build();
    }

    @GetMapping("/get-users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .code(null)
                .success(true).build();
        try {
            apiResponse.setData(usersService.getAllUsers());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AppException("GAU0000", "common.system.error");
        }
        return apiResponse;
    }

    @PostMapping
    public ApiResponse createUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        try {
            usersService.createUser(user);
        } catch (AppException e) {
            logger.error(e.getMessage());
            throw new AppException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AppException("CU00000", "common.system.error");
        }
        return ApiResponse.builder()
                .code(null)
                .success(true)
                .path(request.getRequestURI()).build();
    }
}
