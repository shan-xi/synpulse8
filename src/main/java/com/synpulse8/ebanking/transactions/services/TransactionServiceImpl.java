package com.synpulse8.ebanking.transactions.services;

import com.synpulse8.ebanking.account.repo.AccountRepository;
import com.synpulse8.ebanking.transactions.dto.TransactionDto;
import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import com.synpulse8.ebanking.transactions.entity.Transaction;
import com.synpulse8.ebanking.transactions.repo.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionListRes getTransactionList(String accountUid, LocalDate month, Integer pageNumber, Integer pageSize) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Transaction> spec = Specification.where(null);
        if (StringUtils.isNoneEmpty(accountUid)) {
            var accountOptional = accountRepository.findByUid(accountUid);
            if (accountOptional.isPresent()) {
                spec = spec.and(TransactionSpecifications.accountEquals(accountOptional.get()));
            } else {
                return new TransactionListRes(pageNumber, pageSize, 0, 0L, List.of());
            }
        }
        var transactionPageData = transactionRepository.findAll(spec, pageable);
        return new TransactionListRes(
                transactionPageData.getNumber(),
                transactionPageData.getSize(),
                transactionPageData.getTotalPages(),
                transactionPageData.getTotalElements(),
                transactionPageData.getContent().stream()
                        .map(transaction -> new TransactionDto(transaction.getTransactionId())).toList()
        );
    }
}
