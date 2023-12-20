package com.synpulse8.ebanking.dao.transaction.repo;

import com.synpulse8.ebanking.dao.transaction.dto.TransactionDto;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.utils.CustomDtoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, CustomDtoRepository<Transaction, TransactionDto> {
}
