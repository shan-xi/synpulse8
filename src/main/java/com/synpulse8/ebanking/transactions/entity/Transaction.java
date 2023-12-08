package com.synpulse8.ebanking.transactions.entity;

import com.synpulse8.ebanking.account.entity.Account;
import com.synpulse8.ebanking.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @Column(name = "transaction_id", columnDefinition = " VARCHAR(36) COMMENT 'Transaction ID' ")
    private String transactionId;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "currency", columnDefinition = "SMALLINT COMMENT 'currency'")
    private Currency currency;
    @Column(name = "amount", columnDefinition = "DOUBLE COMMENT 'Amount' ")
    private Double amount;
    @Column(name = "iban", columnDefinition = " varchar(26) COMMENT 'IBAN' ")
    private String iban;
    @Column(name = "value_date", columnDefinition = " timestamp COMMENT 'Value Date' ")
    private Date valueDate;
    @Column(name = "description", columnDefinition = " varchar(100) COMMENT 'Description' ")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @BatchSize(size = 100)
    @JoinColumn(name = "account_uid", nullable = false)
    private Account account;
}
