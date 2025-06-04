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
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private Long price;
    private double rating;
    private Long bookStatusInStock; // 1: Available, 0: Unavailable
    private Long bookFormat; // 1:Bia cung | 2: eBook
    private String description;
    private String imageUrl;
    private Date insertDate; // Ngay nhap kho
    private String bookCode;
    private String insertUser;
    private int quantity; // So luong sach trong kho
}
