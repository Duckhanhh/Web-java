package org.bpm.abcbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ABC_BOOK", name = "Books")
public class Books {
    public static final Long BOOK_STATUS_ACTIVE = 1L;
    public static final Long BOOK_STATUS_INACTIVE = 0L;
    public static final Long BOOK_FORMAT_HARDCOVER = 1L;
    public static final Long BOOK_FORMAT_EBOOK = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "price")
    private Long price;
    @Column(name = "rating_average")
    private Double ratingAverage;
    @Column(name = "publication_date")
    private String publicationDate;
    @Column(name = "supplier_code")
    private String publisher;
    @Column(name = "book_status")
    private Long bookStatus; // 1:Hieu luc | 0: Ko hieu luc
    @Column(name = "book_format")
    private Long bookFormat; // 1:Bia cung | 2: eBook
    @Column(name = "author")
    private String author;
    @Column(name = "category")
    private String category;
    @Column(name = "add_date")
    private Date addDate;
}
