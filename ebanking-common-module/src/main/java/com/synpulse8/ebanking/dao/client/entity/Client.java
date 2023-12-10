package com.synpulse8.ebanking.dao.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synpulse8.ebanking.dao.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Client", indexes = @Index(columnList = "name"), uniqueConstraints = @UniqueConstraint(columnNames = {"uid"}))
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
    @OneToMany(mappedBy = "client", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Account> accountList;

    @JsonIgnore
    public void addAccountList(Account account) {
        if (CollectionUtils.isEmpty(this.accountList)) {
            this.accountList = new ArrayList<>();
        }
        this.accountList.add(account);
    }
}
