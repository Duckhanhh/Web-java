package org.bpm.abcbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(schema = "ABC_BOOK", name = "Orders")
@DynamicUpdate
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "total_amount")
    private Long totalAmount;
    @Column(name = "shipping_fee")
    private Long shippingFee;
    @Column(name = "discount_amount")
    private Long discountAmount;
    @Column(name = "payment_method")
    private Long paymentMethod; // 1: tra truoc, 2: tra sau
    @Column(name = "status")
    private Long status; // 1: dang thuc hien | 3: da hoan thanh | 4: da huy
    @Column(name = "state")
    private Long state; // 1: dang cho tiep nhan | 2: dang cho giao hang | 3: dang giao hang | 4: da giao hang
    @Column(name = "shipping_carrier")
    private String shippingCarrier;
    @Column(name = "shipment_date")
    private Date shipmentDate;
    @Column(name = "data")
    private String data; // JSON luu tru thong tin don hang
    @Column(name = "staff_confirm")
    private String staffConfirm; // Nhan vien xac nhan don hang
    @Column(name = "shipping_address")
    private String shippingAddress; // Dia chi giao hang
    @Column(name = "pay_status")
    private Long payStatus; // 1: da thanh toan, 0: chua thanh toan
    @Column(name = "complete_date")
    private Date completeDate; // Ngay hoan thanh don hang
}
