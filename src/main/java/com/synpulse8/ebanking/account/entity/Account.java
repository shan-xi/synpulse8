package com.synpulse8.ebanking.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", indexes = @Index(columnList = "name"))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = " varchar(20) COMMENT 'account name' ")
    private String name;

    @Column(name = "email", columnDefinition = " varchar(100) COMMENT 'email' ")
    private String email;

    @Column(name = "password", columnDefinition = " varchar(100) COMMENT 'password' ")
    private String password;

    public Account(String email) {
        this.email = email;
    }
}
