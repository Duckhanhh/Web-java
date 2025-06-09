package org.bpm.abcbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private Long bookId;
    private String bookName;
    private Long quantity;
    private Long unitPrice;
}
