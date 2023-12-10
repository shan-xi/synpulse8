package com.synpulse8.ebanking.dao.account.repo;

import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUid(String uid);

    Optional<Account> findByUidAndCurrency(String uid, Currency currency);
}
