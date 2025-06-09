package org.bpm.abcbook.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ApiResponse<String> placeOrder(@RequestBody String orderData) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .build();
        try {
            String orderId = orderService.placeOrder(orderData);
            response.setData(orderId);
        } catch (Exception e) {
            log.error("Error placing order", e);
            response.setSuccess(false);
            response.setMessage("Lỗi khi đặt hàng: " + e.getMessage());
        }
        return response;
    }
}
