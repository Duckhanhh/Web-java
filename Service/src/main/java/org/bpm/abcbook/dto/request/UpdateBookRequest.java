package org.bpm.abcbook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookRequest {
    private String title;
    private String author;
    private String description;
    private String imageUrl;
    private Long price;
    private String publisher;
    private Long bookStatus;
    private Long bookFormat;
    private String category;
}
