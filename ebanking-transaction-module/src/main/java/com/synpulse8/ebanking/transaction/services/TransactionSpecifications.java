package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class TransactionSpecifications {
    public static Specification<Transaction> accountEquals(Account account) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account"), account);
    }

    public static Specification<Transaction> accountIn(List<Account> accountList) {
        return (root, query, criteriaBuilder) ->
                root.get("account").in(accountList);
    }

    public static Specification<Transaction> valueDateGreaterThanOrEqualTo(LocalDate valueDate) {
        var timestamp = Timestamp.valueOf(valueDate.atStartOfDay());
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("valueDate"), timestamp);
    }

    public static Specification<Transaction> valueDateLessThan(LocalDate valueDate) {
        var timestamp = Timestamp.valueOf(valueDate.plusDays(1L).atStartOfDay());
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("valueDate"), timestamp);
    }
}