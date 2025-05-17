package org.bpm.abcbook.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.bpm.abcbook.dto.request.AuthenticationRequest;
import org.bpm.abcbook.dto.request.IntrospectRequest;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/test")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute @Valid AuthenticationRequest authenticationRequest, BindingResult bindingResult,
                        HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        try {
            if (bindingResult.hasErrors()) {
                logger.info("Lỗi validation: {}", bindingResult.getAllErrors());
                bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
                model.addAttribute("errors", errors);
                model.addAttribute("authenticationRequest", authenticationRequest);
                request.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
                // forward đến login.html, thường qua một Servlet hoặc bộ render nào đó
                request.getRequestDispatcher("/login.html").forward(request, response);
                return "login.html";
            }

            authenticationService.login(authenticationRequest);
            return "redirect:/";
        } catch (AppException e) {
            logger.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "login.html";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("error", "Hệ thống gặp lỗi, vui lòng thử lại.");
            return "login.html";
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
