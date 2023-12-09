package com.synpulse8.ebanking.dao.account.entity;

import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", indexes = @Index(columnList = "name"))
public class Account {
    @Id
    @Column(name = "uid", columnDefinition = " VARCHAR(12) COMMENT 'account uid' ")
    private String uid;
    @Column(name = "name", columnDefinition = " varchar(20) COMMENT 'account name' ")
    private String name;
    @Column(name = "password", columnDefinition = " varchar(100) COMMENT 'password' ")
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "currency", columnDefinition = "SMALLINT COMMENT 'currency'")
    private Currency currency;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionList;

    public Account(String uid) {
        this.uid = uid;
    }
}
