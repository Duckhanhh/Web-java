package org.bpm.abcbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ABC_BOOK", name = "Inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "insert_user")
    private String insertUser;
    @Column(name = "insert_date")
    private Date insertDate;
    @Column(name = "update_user_code")
    private String updateUser;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    private Long status; // 0: inactive, 1: active
}
