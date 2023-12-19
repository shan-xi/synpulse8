package com.synpulse8.ebanking.dao.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synpulse8.ebanking.dao.account.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Client", indexes = @Index(columnList = "name"), uniqueConstraints = @UniqueConstraint(columnNames = {"uid"}))
@Cache(region = "CLIENT-CACHE", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uid", columnDefinition = " VARCHAR(12) COMMENT 'account uid' ")
    private String uid;
    @Column(name = "name", columnDefinition = " varchar(20) COMMENT 'account name' ")
    private String name;
    @Column(name = "password", columnDefinition = " varchar(100) COMMENT 'password' ")
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Account> accountList;

    @JsonIgnore
    public void addAccountList(Account account) {
        if (CollectionUtils.isEmpty(this.accountList)) {
            this.accountList = new ArrayList<>();
        }
        this.accountList.add(account);
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
