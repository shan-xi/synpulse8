package com.synpulse8.ebanking.transactions.services;

import com.synpulse8.ebanking.account.repo.AccountRepository;
import com.synpulse8.ebanking.transactions.dto.TransactionDto;
import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import com.synpulse8.ebanking.transactions.dto.TransactionSearchDto;
import com.synpulse8.ebanking.transactions.entity.Transaction;
import com.synpulse8.ebanking.transactions.repo.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public TransactionListRes getTransactionList(TransactionSearchDto dto) {
        var pageable = PageRequest.of(dto.pageNumber(), dto.pageSize());
        Specification<Transaction> spec = Specification.where(null);
        if (StringUtils.isNoneEmpty(dto.accountId())) {
            var accountOptional = accountRepository.findByUid(dto.accountId());
            if (accountOptional.isPresent()) {
                spec = spec.and(TransactionSpecifications.accountEquals(accountOptional.get()));
            } else {
                return new TransactionListRes(dto.pageNumber(), dto.pageSize(), 0, 0L, List.of());
            }
        }
        if (Objects.nonNull(dto.startDate())) {
            spec = spec.and(TransactionSpecifications.valueDateGreaterThanOrEqualTo(dto.startDate()));
        }
        if (Objects.nonNull(dto.endDate())) {
            spec = spec.and(TransactionSpecifications.valueDateLessThan(dto.endDate()));
        }
        var transactionPageData = transactionRepository.findAll(spec, pageable);
        return new TransactionListRes(
                transactionPageData.getNumber(),
                transactionPageData.getSize(),
                transactionPageData.getTotalPages(),
                transactionPageData.getTotalElements(),
                transactionPageData.getContent().stream()
                        .map(transaction -> new TransactionDto(
                                transaction.getTransactionId(),
                                transaction.getCurrency(),
                                transaction.getAmount(),
                                transaction.getIban(),
                                transaction.getValueDate(),
                                transaction.getDescription(),
                                transaction.getAccount().getUid()
                        )).toList()
        );
    }
}
