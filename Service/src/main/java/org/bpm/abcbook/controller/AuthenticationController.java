package org.bpm.abcbook.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import org.bpm.abcbook.dto.request.AuthenticationRequest;
import org.bpm.abcbook.dto.request.IntrospectRequest;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@Component
@ViewScoped
@Named
@Data
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private String email;
    private String password;
    private String errorMessage;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    public String login() throws Exception {
        try {
            authenticationService.login(new AuthenticationRequest(email, password));
            return "dashboard?faces-redirect=true";
        } catch (Exception e) {
            logger.error(e.getMessage());
            errorMessage = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thông tin lỗi", e.getMessage()));
            return null; // Trả về null để không chuyển hướng
        }
    }

    @PostMapping("introspect")
    public ApiResponse introspect(@RequestBody @Valid IntrospectRequest introspectRequest, HttpServletRequest request) {
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
