package com.synpulse8.ebanking.dao.account.repo;

import com.synpulse8.ebanking.dao.account.dto.AccountDto;
import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUidAndCurrency(String uid, Currency currency);

    @Query("SELECT new com.synpulse8.ebanking.dao.account.dto.AccountDto(" +
            "a.id," +
            "a.uid," +
            "a.currency," +
            "a.iban," +
            "a.clientId) " +
            "FROM Account a " +
            "WHERE a.clientId = :clientId")
    List<AccountDto> findAccountDtoByClientId(@Param("clientId") Long clientId);
}
