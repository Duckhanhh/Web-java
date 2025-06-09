package org.bpm.abcbook.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Long orderId;
    private Long userId;
    private String userFirstName;
    private Date orderDate;
    private Long totalAmount;
    private Long shippingFee;
    private Long productFee;
    private Long discountAmount;
    private Long paymentMethod; // 1: tra truoc, 2: tra sau
    private Long status; // 1: dang thuc hien | 3: da hoan thanh | 4: da huy
    private Long state; // 1: dang cho tiep nhan | 2: dang cho giao hang | 3: dang giao hang | 4: da giao hang
    private String shippingCarrier;
    private Date shipmentDate;
    private List<OrderItemDTO> listOrderItem;
    private String staffConfirm; // Nhan vien xac nhan don hang
    private String staffFirstName;
    private String shippingAddress; // Dia chi giao hang
    private Long payStatus; // 1: da thanh toan, 0: chua thanh toan
    private Date completeDate; // Ngay hoan thanh don hang

    public void getNecessaryData(String data) throws Exception {
        OrderDTO order = objectMapper.readValue(data, OrderDTO.class);
        this.setListOrderItem(order.getListOrderItem());
        this.setProductFee(order.getProductFee());
    }
}
