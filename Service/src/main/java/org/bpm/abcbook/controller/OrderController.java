package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.Const;
import org.bpm.abcbook.dto.OrderDTO;
import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.model.User;
import org.bpm.abcbook.service.OrderService;
import org.bpm.abcbook.service.StaffService;
import org.bpm.abcbook.service.UserService;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@Data
@ViewScoped
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private Long orderId;
    private Long payMethod;
    private Long payStatus;
    private List<User> listUser;
    private List<Long> listUserSelected;
    private List<Long> orderSelectedStatus;
    private List<String> listShippingCarrier;
    private List<String> listSelectedShippingCarrier;
    private List<StaffResponse> listStaff;
    private List<String> listSelectedStaff;
    private List<Date> listSelectedDate;
    private List<OrderDTO> listOrders;
    private List<Long> listSelectedState;
    private OrderDTO selectedOrder;
    private Long expandedOrderId;
    private String currentStaffCode;

    @Autowired
    private UserService userService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OrderService orderService;
    @Inject
    private UserSessionBean userSessionBean;

    @PostConstruct
    public void init() {
        try {
            clearDataSearch();
            listUser = userService.findAllUsers();
            listStaff = staffService.getAllStaffs();
            listShippingCarrier = Const.LIST_SHIPPING_CARRIERS;
            currentStaffCode = userSessionBean == null || userSessionBean.getCurrentStaff() == null ?
                    null : userSessionBean.getCurrentStaff().getStaffCode();

        } catch (Exception e) {
            logger.error("Error initializing OrderController", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi hệ thống", ""));
        }
    }

    public void searchOrders() {
        try {
            Date fromDate = null;
            Date toDate = null;
            if (listSelectedDate != null && !listSelectedDate.isEmpty()) {
                fromDate = listSelectedDate.getFirst();
                toDate = listSelectedDate.getLast();
            }

            listOrders = orderService.findOrder(
                    orderId,
                    payMethod,
                    payStatus,
                    listUserSelected,
                    orderSelectedStatus,
                    listSelectedShippingCarrier,
                    listSelectedStaff,
                    listSelectedState,
                    fromDate,
                    toDate
            );

            if (listOrders == null || listOrders.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Không tìm thấy kết quả nào", ""));
            }
        } catch (Exception e) {
            logger.error("Error searching orders", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi tìm kiếm đơn hàng", ""));
        }
    }

    public void prepareEdit(OrderDTO order) {
        try {
            if (order == null || order.getOrderId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Đơn hàng không hợp lệ", ""));
                return;
            }
            this.selectedOrder = order;
        } catch (Exception e) {
            logger.error("Error preparing edit for order: {}", order, e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi chuẩn bị chỉnh sửa đơn hàng", ""));
        }
    }

    public void cancelOrder() {
        try {
            orderService.cancelOrder(selectedOrder.getOrderId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Hủy đơn hàng thành công"));
            searchOrders();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi hủy đơn hàng", ""));
        }
    }

    public void payConfirm() {
        try {
            orderService.payConfirm(selectedOrder.getOrderId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Xác nhận thanh toán thành công"));
            searchOrders();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi xác nhận thanh toán", ""));
        }
    }

    public void updateStateOrder(Long state) {
        try {

            if (selectedOrder == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi cập nhật đơn hàng", ""));
                return;
            }

            Long orderId = selectedOrder.getOrderId();

            if (Const.OrderState.ORDER_STATE_WAIT_FOR_DELIVERY.equals(state)) {
                orderService.receiveOrder(orderId, currentStaffCode);
            } else if (Const.OrderState.ORDER_STATE_DELIVERING.equals(state)) {
                orderService.deliveryOrder(orderId);
            } else if (Const.OrderState.ORDER_STATE_DELIVERED.equals(state)) {
                orderService.completeOrder(orderId, Const.PayStatus.PAID);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cập nhật thành công"));
            searchOrders();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi khi cập nhật đơn hàng", ""));
        }
    }

    public void prepareView(ToggleEvent event) {
        try {
            OrderDTO toggledOrder = (OrderDTO) event.getData();

            if (event.getVisibility() == Visibility.VISIBLE) {
                // Mở hàng mới
                if (expandedOrderId == null || !expandedOrderId.equals(toggledOrder.getOrderId())) {
                    this.selectedOrder = toggledOrder;
                    this.expandedOrderId = toggledOrder.getOrderId();
                }
            } else {
                // Đóng hàng hiện tại
                if (this.expandedOrderId != null && this.expandedOrderId.equals(toggledOrder.getOrderId())) {
                    this.selectedOrder = null;
                    this.expandedOrderId = null;
                }
            }
        } catch (Exception e) {
            logger.error("Error in prepareView", e);
        }
    }

    public boolean isRowExpanded(OrderDTO order) {
        return expandedOrderId != null && expandedOrderId.equals(order.getOrderId());
    }

    public void clearDataSearch() {
        listUserSelected = new ArrayList<>();
        orderId = null;
        payMethod = null;
        payStatus = null;
        orderSelectedStatus = new ArrayList<>();
        listSelectedShippingCarrier = new ArrayList<>();
        listSelectedStaff = new ArrayList<>();
        listSelectedDate = new ArrayList<>();
        listSelectedState = new ArrayList<>();
    }

    public String getOrderStatusName(Long status) {
        if (status == null) {
            return null;
        }
        if (Const.OrderStatus.ORDER_STATUS_IN_PROGRESS.equals(status)) {
            return "Đang thực hiện";
        } else if (Const.OrderStatus.ORDER_STATUS_COMPLETED.equals(status)) {
            return "Đã hoàn thành";
        } else if (Const.OrderStatus.ORDER_STATUS_CANCELLED.equals(status)) {
            return "Đã hủy";
        } else {
            return "Không xác định";
        }
    }

    public String getOrderStateName(Long state) {
        if (state == null) {
            return null;
        }
        if (Const.OrderState.ORDER_STATE_PENDING.equals(state)) {
            return "Đang chờ tiếp nhận";
        } else if (Const.OrderState.ORDER_STATE_WAIT_FOR_DELIVERY.equals(state)) {
            return "Đang chờ giao hàng";
        } else if (Const.OrderState.ORDER_STATE_DELIVERING.equals(state)) {
            return "Đang giao hàng";
        } else if (Const.OrderState.ORDER_STATE_DELIVERED.equals(state)) {
            return "Đã giao hàng";
        } else {
            return "Không xác định";
        }
    }

    public String getPayMethodName(Long payMethod) {
        if (payMethod == null) {
            return null;
        }
        if (payMethod == 1L) {
            return "Trả trước";
        } else if (payMethod == 2L) {
            return "Trả sau";
        } else {
            return "Không xác định";
        }
    }

    public String getPayStatusName(Long payStatus) {
        if (payStatus == null) {
            return null;
        }
        if (payStatus == 1L) {
            return "Đã thanh toán";
        } else if (payStatus == 0L) {
            return "Chưa thanh toán";
        } else {
            return "Không xác định";
        }
    }
}
