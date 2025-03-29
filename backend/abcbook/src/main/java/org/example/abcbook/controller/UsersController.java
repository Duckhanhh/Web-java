package org.example.abcbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.abcbook.dto.request.UserRequest;
import org.example.abcbook.dto.response.ApiResponse;
import org.example.abcbook.model.Users;
import org.example.abcbook.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.net.BindException;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    UsersService usersService;

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable("id") Long id) {
        return usersService.getUserById(id);
    }

    @PostMapping
    public ApiResponse createUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        usersService.createUser(user);
        return ApiResponse.builder()
                .code(123)
                .success(true)
                .path(request.getRequestURI()).build();
    }
}
