package org.bpm.abcbook.service;

import org.bpm.abcbook.dto.*;
import org.bpm.abcbook.model.Orders;

import java.util.Date;
import java.util.List;

public interface OrderService {
    String placeOrder(String orderData) throws Exception;

    List<OrderDTO> findOrder(Long orderId, Long payMethod, Long payStatus, List<Long> listUserId, List<Long> listStatus,
                             List<String> listShippingCarrier, List<String> listStaff, List<Long> listState,
                             Date fromDate, Date toDate) throws Exception;

    void updateOrder(Orders order) throws Exception;

    void receiveOrder(Long orderId, String staffConfirm) throws Exception;

    void deliveryOrder(Long orderId) throws Exception;

    void completeOrder(Long orderId, Long recentPayStatus) throws Exception;

    void cancelOrder(Long orderId) throws Exception;

    void payConfirm(Long orderId) throws Exception;

    List<RevenueDTO> getRevenue() throws Exception;

    List<CategoryDTO> getFavoriteCategory() throws Exception;

    List<BookDTO> getBestSellingBooks() throws Exception;

    List<NumberOrderDTO> getNumberOrder() throws Exception;
}
