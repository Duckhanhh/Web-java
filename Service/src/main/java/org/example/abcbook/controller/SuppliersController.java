package org.example.abcbook.controller;

import org.example.abcbook.dto.response.ApiResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.model.Suppliers;
import org.example.abcbook.service.SuppliersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SuppliersController {
    private static final Logger logger = LoggerFactory.getLogger(SuppliersController.class);

    @Autowired
    private SuppliersService suppliersService;

    @GetMapping("/findAll")
    public ApiResponse<List<Suppliers>> findAll() {
        ApiResponse<List<Suppliers>> apiResponse = ApiResponse.<List<Suppliers>>builder()
                .success(true).build();
        try {
            apiResponse.setData(suppliersService.getAllSuppliers());
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new AppException("SFA00001", exception.getMessage());
        }
        return apiResponse;
    }
}
