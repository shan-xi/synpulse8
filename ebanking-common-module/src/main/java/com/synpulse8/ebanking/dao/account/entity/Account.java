package com.synpulse8.ebanking.dao.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synpulse8.ebanking.dao.client.entity.Client;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.enums.Currency;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = {"uid", "currency"}), @UniqueConstraint(columnNames = {"iban"})})
@Cache(region = "ACCOUNT-CACHE", usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @BatchSize(size = 100)
    @JoinColumn(name = "client_id")
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
