package org.bpm.abcbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bpm.abcbook.dto.request.StaffRequest;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    StaffService staffService;

    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable("id") Long id) {
        return ApiResponse.builder()
                .code(null)
                .success(true)
                .data(staffService.getUserById(id)).build();
    }

    @GetMapping("/get-users")
    public ApiResponse<List<StaffResponse>> getAllUsers() {
        ApiResponse<List<StaffResponse>> apiResponse = ApiResponse.<List<StaffResponse>>builder()
                .code(null)
                .success(true).build();
        try {
            apiResponse.setData(staffService.getAllUsers());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AppException("GAU0000", "common.system.error");
        }
        return apiResponse;
    }

    @PostMapping
    public ApiResponse createUser(@RequestBody @Valid StaffRequest staffRequest, HttpServletRequest request) {
        try {
            staffService.createStaff(staffRequest);
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
