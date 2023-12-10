package com.synpulse8.ebanking.dao.account.entity;

import com.synpulse8.ebanking.dao.client.entity.Client;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = {"uid", "currency"}), @UniqueConstraint(columnNames = {"iban"})})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uid", columnDefinition = " VARCHAR(12) COMMENT 'account uid' ")
    private String uid;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", columnDefinition = "VARCHAR(3) COMMENT 'Currency'")
    private Currency currency;
    @Column(name = "iban", columnDefinition = " varchar(26) COMMENT 'IBAN' ")
    private String iban;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Transaction> transactionList;
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @BatchSize(size = 100)
    @JoinColumn(name = "client_id")
    private Client client;
}
