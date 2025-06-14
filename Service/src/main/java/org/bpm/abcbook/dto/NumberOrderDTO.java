package org.bpm.abcbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumberOrderDTO {
    private Long totalQuantity;
    private Long NumberCompletedOrders;
    private String month;
}
