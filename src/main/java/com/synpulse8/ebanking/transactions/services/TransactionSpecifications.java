package com.synpulse8.ebanking.transactions.services;

import com.synpulse8.ebanking.account.entity.Account;
import com.synpulse8.ebanking.transactions.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecifications {
    public static Specification<Transaction> accountEquals(Account account) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account"), account);
    }
}
