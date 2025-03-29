package org.example.abcbook.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ABC_BOOK", name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // nhan biet tu dong tao boi mysql
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password_hash")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "registration_date")
    private Date registrationDate;
}
