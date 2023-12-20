package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.dao.account.dto.AccountDto;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.dao.transaction.dto.TransactionDto;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.transaction.field.FieldTransactionAccountId;
import com.synpulse8.ebanking.dao.transaction.field.FieldTransactionValueDate;
import com.synpulse8.ebanking.dao.transaction.repo.TransactionRepository;
import com.synpulse8.ebanking.dao.utils.*;
import com.synpulse8.ebanking.security.PrincipleUtils;
import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import com.synpulse8.ebanking.transaction.dto.TransactionRes;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final ExchangeRateService exchangeRateService;
    private final PrincipleUtils principleUtils;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  ClientRepository clientRepository,
                                  ExchangeRateService exchangeRateService,
                                  PrincipleUtils principleUtils,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.exchangeRateService = exchangeRateService;
        this.principleUtils = principleUtils;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionListRes getTransactionList(TransactionSearchDto dto) {

        var pageRequest = PageRequest.of(dto.pageNumber(), dto.pageSize());
        var conditionList = new ArrayList<Condition<Field<?>>>();
        var uid = principleUtils.getUid();
        var clientOptional = clientRepository.findClientDtoByUid(uid);
        if (clientOptional.isPresent()) {
            var client = clientOptional.get();
            var accountIdList = accountRepository.findAccountDtoByClientId(client.id()).stream().map(AccountDto::id).toList();
            conditionList.add(new Condition<>(new FieldTransactionAccountId(accountIdList), SqlOperator.IN));
        } else {
            return new TransactionListRes(dto.pageNumber(), dto.pageSize(), 0, 0L, dto.baseCurrency(), List.of());
        }
        if (Objects.nonNull(dto.startDate())) {
            conditionList.add(new Condition<>(new FieldTransactionValueDate(dto.startDate()), SqlOperator.GREATER_THAN_OR_EQUAL_TO_LOCAL_DATE));
        }
        if (Objects.nonNull(dto.endDate())) {
            conditionList.add(new Condition<>(new FieldTransactionValueDate(dto.endDate()), SqlOperator.LESS_THAN_OR_EQUAL_TO_LOCAL_DATE));
        }
        var conditionGroup = new ConditionGroup(conditionList, LogicOperator.AND);
        var transactionPageData = transactionRepository.getList(Transaction.class, TransactionDto.class, pageRequest, List.of(conditionGroup));
        var exchangeRates = exchangeRateService.getExchangeRates(dto.baseCurrency().name());
        return new TransactionListRes(
                transactionPageData.getNumber(),
                transactionPageData.getSize(),
                transactionPageData.getTotalPages(),
                transactionPageData.getTotalElements(),
                dto.baseCurrency(),
                transactionPageData.getContent().stream()
                        .map(obj -> {
                            var transaction = (TransactionDto) obj;
                            var currencyName = transaction.currency().name();
                            var conversionRate = exchangeRates.get(currencyName);
                            if (Objects.isNull(conversionRate)) {
                                return null;
                            }
                            var exchangeRate = 1 / conversionRate;
                            var accountOptional = accountRepository.findById(transaction.accountId());
                            var accountUid = "";
                            if (accountOptional.isPresent()) {
                                accountUid = accountOptional.get().getUid();
                            }
                            return new TransactionRes(
                                    transaction.transactionId(),
                                    transaction.accountId() * exchangeRate,
                                    transaction.balanceChange(),
                                    transaction.iban(),
                                    transaction.valueDate(),
                                    transaction.description(),
                                    accountUid
                            );
                        })
                        .filter(Objects::nonNull)
                        .toList()
        );
    }
}
