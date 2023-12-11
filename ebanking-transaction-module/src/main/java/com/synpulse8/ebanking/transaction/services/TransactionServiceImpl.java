package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.transaction.repo.TransactionRepository;
import com.synpulse8.ebanking.security.PrincipleUtils;
import com.synpulse8.ebanking.transaction.dto.TransactionDto;
import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final ExchangeRateService exchangeRateService;
    private final PrincipleUtils principleUtils;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  ClientRepository clientRepository,
                                  ExchangeRateService exchangeRateService,
                                  PrincipleUtils principleUtils) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.exchangeRateService = exchangeRateService;
        this.principleUtils = principleUtils;
    }

    @Override
    public TransactionListRes getTransactionList(TransactionSearchDto dto) {
        var pageable = PageRequest.of(dto.pageNumber(), dto.pageSize());
        Specification<Transaction> spec = Specification.where(null);

        var uid = principleUtils.getUid();
        var clientOptional = clientRepository.findByUid(uid);
        if (clientOptional.isPresent() && !CollectionUtils.isEmpty(clientOptional.get().getAccountList())) {
            var accountList = clientOptional.get().getAccountList();
            spec = spec.and(TransactionSpecifications.accountIn(accountList));
        } else {
            return new TransactionListRes(dto.pageNumber(), dto.pageSize(), 0, 0L, dto.baseCurrency(), List.of());
        }
        if (Objects.nonNull(dto.startDate())) {
            spec = spec.and(TransactionSpecifications.valueDateGreaterThanOrEqualTo(dto.startDate()));
        }
        if (Objects.nonNull(dto.endDate())) {
            spec = spec.and(TransactionSpecifications.valueDateLessThan(dto.endDate()));
        }
        var transactionPageData = transactionRepository.findAll(spec, pageable);
        var exchangeRates = exchangeRateService.getExchangeRates(dto.baseCurrency().name());
        return new TransactionListRes(
                transactionPageData.getNumber(),
                transactionPageData.getSize(),
                transactionPageData.getTotalPages(),
                transactionPageData.getTotalElements(),
                dto.baseCurrency(),
                transactionPageData.getContent().stream()
                        .map(transaction -> {
                            var currencyName = transaction.getCurrency().name();
                            var conversionRate = exchangeRates.get(currencyName);
                            if (Objects.isNull(conversionRate)) {
                                return null;
                            }
                            var exchangeRate = 1 / conversionRate;
                            return new TransactionDto(
                                    transaction.getTransactionId(),
                                    transaction.getAmount() * exchangeRate,
                                    transaction.getBalanceChange(),
                                    transaction.getIban(),
                                    transaction.getValueDate(),
                                    transaction.getDescription(),
                                    transaction.getAccount().getUid()
                            );
                        })
                        .filter(Objects::nonNull)
                        .toList()
        );
    }
}
