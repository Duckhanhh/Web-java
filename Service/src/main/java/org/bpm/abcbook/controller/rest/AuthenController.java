package org.bpm.abcbook.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bpm.abcbook.controller.AuthenticationController;
import org.bpm.abcbook.dto.request.IntrospectRequest;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenController {
    public static final Logger logger = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/introspect")
    public ApiResponse introspect(@RequestBody
                                  @Valid IntrospectRequest introspectRequest, HttpServletRequest request) {
        try {
            return ApiResponse.builder()
                    .code(null)
                    .success(true)
                    .data(authenticationService.introspect(introspectRequest))
                    .path(request.getRequestURI()).build();
        } catch (AppException e) {
            logger.error(e.getMessage());
            throw new AppException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AppException("CU00000", "common.system.error");
        }
    }
}
