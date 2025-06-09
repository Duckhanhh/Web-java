package org.bpm.abcbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bpm.abcbook.Const;
import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.dto.OrderDTO;
import org.bpm.abcbook.dto.OrderItemDTO;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.OrderMapper;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.model.Orders;
import org.bpm.abcbook.repository.BooksRepo;
import org.bpm.abcbook.repository.InventoryRepo;
import org.bpm.abcbook.repository.OrdersRepo;
import org.bpm.abcbook.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BooksRepo booksRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrdersRepo ordersRepo;
    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String placeOrder(String orderData) throws Exception {
        if ("".equals(orderData)) {
            throw new AppException("PO00001", "Dữ liệu đơn hàng bị bỏ trống");
        }
        OrderDTO orderDTO = objectMapper.readValue(orderData, OrderDTO.class);
        if (orderDTO == null) {
            throw new AppException("PO00002", "Chuyển đổi dữ liệu đơn hàng không thành công");
        }

        List<OrderItemDTO> listOrderItem = orderDTO.getListOrderItem();
        if (listOrderItem == null || listOrderItem.isEmpty()) {
            throw new AppException("PO00003", "Danh sách mặt hàng trong đơn hàng không được để trống");
        }

        //Kiem tra nguoi dung
        userRepo.findById(orderDTO.getUserId())
                .orElseThrow(() -> new AppException("PO00006", "Người dùng không tồn tại"));

        //Kiem tra sach va tinh toan tong tien
        Long totalAmount;
        Long productFee = 0L;
        for (OrderItemDTO item : listOrderItem) {
            if (item == null) {
                continue;
            }
            if (item.getBookId() == null || item.getQuantity() <= 0) {
                throw new AppException("PO00004", "Thông tin mặt hàng không hợp lệ: " + item.getBookId());
            }

            BookDTO bookInStock = inventoryRepo.findAllInStock(null, null, null, Const.BookStatus.BOOK_STATUS_AVAILABLE,
                    Collections.singletonList(item.getBookId()), null, null, null,
                    null, null, null, null).stream()
                    .findFirst().orElseThrow(() -> new AppException("PO00005", "Không có trong kho mã sách: " + item.getBookId()));


            if (item.getQuantity() > bookInStock.getQuantity()) {
                throw new AppException("PO00006", "Số lượng sách trong kho không đủ");
            }

            Long bookPrice = bookInStock.getPrice();
            item.setUnitPrice(bookPrice);
            productFee += bookPrice * item.getQuantity();
        }
        orderDTO.setProductFee(productFee);

        long discountAmount = orderDTO.getDiscountAmount() == null ? 0L : orderDTO.getDiscountAmount();
        long shippingFee = orderDTO.getShippingFee() == null ? 0L : orderDTO.getShippingFee();

        //Tinh them tien van chuyen va giam gia
        totalAmount = productFee - discountAmount + shippingFee;
        orderDTO.setTotalAmount(totalAmount);

        String data = objectMapper.writeValueAsString(orderDTO);

        Orders newOrder = orderMapper.OrderDTOToOrders(orderDTO);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setData(data);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(Const.OrderStatus.ORDER_STATUS_IN_PROGRESS);
        newOrder.setState(Const.OrderState.ORDER_STATE_PENDING);
        ordersRepo.save(newOrder);

        return newOrder.getOrderId().toString();
    }

    @Override
    public List<OrderDTO> findOrder(Long orderId, Long payMethod, Long payStatus, List<Long> listUserId, List<Long> listStatus,
                                    List<String> listShippingCarrier, List<String> listStaff, List<Long> listState, Date fromDate, Date toDate) throws Exception {
        List<OrderDTO> listOrder = ordersRepo.findOrder(orderId, payMethod, payStatus, listUserId, listStatus, listShippingCarrier, listStaff, listState,
                fromDate, toDate);

        //Lay ra danh sach bookId
        List<Long> listBookId = listOrder.stream()
                .flatMap(order -> order.getListOrderItem().stream())
                .map(OrderItemDTO::getBookId)
                .distinct()
                .toList();

        //Lay ra thong tin sach
        List<Books> books = booksRepo.findAllById(listBookId);

        // Tạo map bookId:bookName
        Map<Long, String> bookIdToName = books.stream()
                .collect(Collectors.toMap(Books::getId, Books::getTitle));

        //Map ten sach
        listOrder.forEach(order -> {
            List<OrderItemDTO> listOrderItem = order.getListOrderItem();
            if (listOrderItem != null) {
                listOrderItem.forEach(item -> {
                    if (item.getBookId() != null) {
                        item.setBookName(bookIdToName.get(item.getBookId()));
                    }
                });
            }
        });

        //Lay thong tin nhan vien (neu co)
        List<String> lstStaff = listOrder.stream().map(OrderDTO::getStaffConfirm).filter(Objects::nonNull).toList();

        return listOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Orders order) throws Exception {
        if (order == null) {
            throw new AppException("UD00001", "Không có thông tin đơn hàng");
        }

        if (!ordersRepo.existsById(order.getOrderId())) {
            throw new AppException("UD00002", "Không có đơn hàng mã: " + order.getOrderId());
        }

        ordersRepo.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOrder(Long orderId, String staffConfirm) throws Exception {
        Orders orders = validateOrder(orderId);

        if (!Const.OrderState.ORDER_STATE_PENDING.equals(orders.getState())) {
            throw new AppException("RO00002", "Đơn hàng không ở trạng thái chờ tiếp nhận");
        }

        if ("".equals(staffConfirm)) {
            throw new AppException("RO00003", "Thiếu thông tin nhân viên tiếp nhận");
        }

        //Cap nhat sach trong kho
        OrderDTO orderDTO = objectMapper.readValue(orders.getData(), OrderDTO.class);
        List<OrderItemDTO> listOrderItem = orderDTO.getListOrderItem();
        List<Long> bookIds = listOrderItem.stream()
                .filter(Objects::nonNull)
                .map(OrderItemDTO::getBookId)
                .collect(Collectors.toList());

        List<Inventory> inventories = inventoryRepo.findAllByListBookId(bookIds);
        Map<Long, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getBookId, inv -> inv));

        for (OrderItemDTO orderItemDTO : listOrderItem) {
            if (orderItemDTO == null) {
                continue;
            }
            Inventory inventory = inventoryMap.get(orderItemDTO.getBookId());
            if (inventory == null) {
                throw new AppException("RO00004", "Không có dữ liệu sách trong kho");
            }
            Long numberBookInStock = inventory.getQuantity();
            Long numberBookInOrder = orderItemDTO.getQuantity();
            if (numberBookInOrder > numberBookInStock) {
                throw new AppException("RO00005", "Số lượng sách trong kho không đủ");
            }
            inventory.setQuantity(numberBookInStock - numberBookInOrder);
            inventoryRepo.save(inventory);
        }

        orders.setState(Const.OrderState.ORDER_STATE_WAIT_FOR_DELIVERY);
        orders.setStaffConfirm(staffConfirm);
        this.updateOrder(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliveryOrder(Long orderId) throws Exception {
        Orders orders = validateOrder(orderId);

        if (!Const.OrderState.ORDER_STATE_WAIT_FOR_DELIVERY.equals(orders.getState())) {
            throw new AppException("DO00001", "Đơn hàng không ở trạng thái chờ tiếp nhận");
        }

        orders.setState(Const.OrderState.ORDER_STATE_DELIVERING);
        orders.setShipmentDate(new Date());
        this.updateOrder(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(Long orderId, Long recentPayStatus) throws Exception {
        Orders orders = validateOrder(orderId);

        if (!Const.OrderState.ORDER_STATE_DELIVERING.equals(orders.getState())) {
            throw new AppException("CO00001", "Đơn hàng không ở trạng thái đang giao hàng");
        }

        if (!Const.PayStatus.PAID.equals(orders.getPayStatus()) && Const.PayStatus.PAID.equals(recentPayStatus)) {
            throw new AppException("CO00002", "Đơn hàng chưa thanh toán");
        }

        orders.setState(Const.OrderState.ORDER_STATE_DELIVERED);
        orders.setStatus(Const.OrderStatus.ORDER_STATUS_COMPLETED);
        orders.setCompleteDate(new Date());
        this.updateOrder(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) throws Exception {
        Optional<Orders> orderDB = ordersRepo.findById(orderId);

        Orders orders = orderDB.orElseThrow(() -> new AppException("CAO00001", "Không có đơn hàng: " + orderId));

        //Cap nhat sach trong kho
        OrderDTO orderDTO = objectMapper.readValue(orders.getData(), OrderDTO.class);
        List<OrderItemDTO> listOrderItem = orderDTO.getListOrderItem();
        List<Long> bookIds = listOrderItem.stream()
                .filter(Objects::nonNull)
                .map(OrderItemDTO::getBookId)
                .collect(Collectors.toList());

        List<Inventory> inventories = inventoryRepo.findAllByListBookId(bookIds);
        Map<Long, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getBookId, inv -> inv));

        for (OrderItemDTO orderItemDTO : listOrderItem) {
            if (orderItemDTO == null) {
                continue;
            }
            Inventory inventory = inventoryMap.get(orderItemDTO.getBookId());
            if (inventory == null) {
                throw new AppException("CAO00002", "Không có dữ liệu sách trong kho");
            }
            Long numberBookInStock = inventory.getQuantity();
            Long numberBookInOrder = orderItemDTO.getQuantity();
            if (numberBookInOrder > numberBookInStock) {
                throw new AppException("CAO00003", "Số lượng sách trong kho không đủ");
            }
            inventory.setQuantity(numberBookInStock + numberBookInOrder);
            inventoryRepo.save(inventory);
        }

        orders.setStatus(Const.OrderStatus.ORDER_STATUS_CANCELLED);
        this.updateOrder(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payConfirm(Long orderId) throws Exception {
        Orders orders = validateOrder(orderId);

        if (!Const.PayStatus.UNPAID.equals(orders.getPayStatus())) {
            throw new AppException("PC00001", "Đơn hàng không ở trạng thái chờ thanh toán");
        }

        orders.setPayStatus(Const.PayStatus.PAID);
        this.updateOrder(orders);
    }


    private Orders validateOrder(Long orderId) {
        if (orderId == null) {
            throw new AppException("VO00001", "Thiếu thông tin mã đơn hàng");
        }

        Optional<Orders> orderDB = ordersRepo.findById(orderId);

        Orders orders = orderDB.orElseThrow(() -> new AppException("RO00003", "Không có đơn hàng: " + orderId));

        if (!Const.OrderStatus.ORDER_STATUS_IN_PROGRESS.equals(orders.getStatus())) {
            throw new AppException("VO00002", "Đơn hàng không ở trạng thái đang thực hiện");
        }

        return orders;
    }
}
