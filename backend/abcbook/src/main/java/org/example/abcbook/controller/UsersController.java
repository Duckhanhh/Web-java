package org.example.abcbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.abcbook.dto.request.UserRequest;
import org.example.abcbook.dto.response.ApiResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    UsersService usersService;

    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable("id") Long id) {
        return ApiResponse.builder()
                .code(null)
                .success(true)
                .data(usersService.getUserById(id)).build();
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
