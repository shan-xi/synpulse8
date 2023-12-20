package com.synpulse8.ebanking.dao.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.enums.BalanceChange;
import com.synpulse8.ebanking.enums.Currency;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSACTION")
@Cache(region = "TRANSACTION-CACHE", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction {
    @Id
    @Column(name = "transaction_id", columnDefinition = " VARCHAR(36) COMMENT 'Transaction ID' ")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", columnDefinition = "VARCHAR(3) COMMENT 'currency'")
    private Currency currency;

    @Column(name = "amount", columnDefinition = "DOUBLE COMMENT 'Amount' ")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "balance_change", columnDefinition = "ENUM('CREDIT','DEPOSIT') COMMENT 'Balance Change' ")
    private BalanceChange balanceChange;

    @Column(name = "iban", columnDefinition = " varchar(26) COMMENT 'IBAN' ")
    private String iban;

    @Column(name = "value_date", columnDefinition = " timestamp COMMENT 'Value Date' ")
    private LocalDate valueDate;

    @Column(name = "description", columnDefinition = " varchar(100) COMMENT 'Description' ")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @BatchSize(size = 100)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "account_id", updatable = false, insertable = false, columnDefinition = " BIGINT ")
    private Long accountId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BalanceChange getBalanceChange() {
        return balanceChange;
    }

    public void setBalanceChange(BalanceChange balanceChange) {
        this.balanceChange = balanceChange;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
