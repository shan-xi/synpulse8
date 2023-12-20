package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.dao.account.dto.AccountDto;
import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.dao.client.dto.ClientDto;
import com.synpulse8.ebanking.dao.client.entity.Client;
import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.dao.transaction.dto.TransactionDto;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.transaction.field.FieldTransactionAccountId;
import com.synpulse8.ebanking.dao.transaction.field.FieldTransactionValueDate;
import com.synpulse8.ebanking.dao.transaction.repo.TransactionRepository;
import com.synpulse8.ebanking.dao.utils.*;
import com.synpulse8.ebanking.enums.BalanceChange;
import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.security.PrincipleUtils;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class TransactionServiceImplUnitTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ExchangeRateService exchangeRateService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PrincipleUtils principleUtils;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionList_Successful() {
        // Arrange
        var uid = "P-0123456789";
        var baseCurrency = Currency.USD;
        var s = LocalDate.now().minusDays(7);
        var e = LocalDate.now();
        var searchDto = new TransactionSearchDto(
                s,
                e,
                0,
                10,
                baseCurrency
        );

        var account = new AccountDto(1L, uid, Currency.EUR, "IBAN123", 1L);
        var transactionPage = createTransactionPage(account);

        var client = new ClientDto(1L, uid, "spin");
        PageRequest pageRequest = PageRequest.of(0, 10);
        var conditionList = new ArrayList<Condition<Field<?>>>();
        conditionList.add(new Condition<>(new FieldTransactionAccountId(List.of(account.id())), SqlOperator.IN));
        conditionList.add(new Condition<>(new FieldTransactionValueDate(s), SqlOperator.GREATER_THAN_OR_EQUAL_TO_LOCAL_DATE));
        conditionList.add(new Condition<>(new FieldTransactionValueDate(e), SqlOperator.LESS_THAN_OR_EQUAL_TO_LOCAL_DATE));
        var conditionGroupList = List.of(new ConditionGroup(conditionList, LogicOperator.AND));
        when(clientRepository.findClientDtoByUid(uid)).thenReturn(Optional.of(client));
        when(accountRepository.findAccountDtoByClientId(client.id())).thenReturn(List.of(account));
        when(transactionRepository.getList(Transaction.class, TransactionDto.class, pageRequest, conditionGroupList)).thenReturn(transactionPage);
        when(exchangeRateService.getExchangeRates(baseCurrency.name())).thenReturn(Collections.singletonMap("EUR", 1.2));
        when(principleUtils.getUid()).thenReturn(uid);

        // Act
        var actualResult = transactionService.getTransactionList(searchDto);
        System.out.println(actualResult);

        // Assert
        assertEquals(1, actualResult.transactionResList().size());
        assertEquals(1, actualResult.totalElements());
        assertEquals(0, actualResult.pageNumber());
        assertEquals(10, actualResult.size());
        assertEquals(1, actualResult.totalPages());
    }

    private Page<TransactionDto> createTransactionPage(AccountDto account) {
        var transaction = new TransactionDto(
                String.valueOf(1L),
                Currency.EUR,
                100.0,
                BalanceChange.CREDIT,
                "IBAN123",
                LocalDate.now(),
                "Test Transaction",
                account.id()
        );

        return new PageImpl<>(Collections.singletonList(transaction), PageRequest.of(0, 10), 1);
    }

    @Test
    void getTransactionList_Successful_EmptyTransactionRecord() {
        // Arrange
        var uid = "P-0123456789";
        var baseCurrency = Currency.USD;
        var searchDto = new TransactionSearchDto(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                0,
                10,
                baseCurrency
        );

        var account = new Account();
        account.setUid(uid);
        account.setCurrency(Currency.EUR);
        var transactionPage = createTransactionPageWithEmptyTransactionRecord(account);

        var client = new Client();
        client.setUid(uid);
        client.setAccountList(Collections.singletonList(account));

        when(clientRepository.findByUid(uid)).thenReturn(Optional.of(client));
        PageRequest pageRequest = PageRequest.of(0, 10);
        var conditionList = new ArrayList<Condition<Field<?>>>();
        var ConditionGroupList = List.of(new ConditionGroup(conditionList, LogicOperator.AND));
        when(clientRepository.findByUid(uid)).thenReturn(Optional.of(client));
        when(transactionRepository.getList(Transaction.class, TransactionDto.class, pageRequest, ConditionGroupList)).thenReturn(transactionPage);
        when(exchangeRateService.getExchangeRates(baseCurrency.name())).thenReturn(Collections.singletonMap("EUR", 1.2));
        when(principleUtils.getUid()).thenReturn(uid);

        // Act
        var actualResult = transactionService.getTransactionList(searchDto);

        // Assert
        assertEquals(0, actualResult.transactionResList().size());
        assertEquals(0, actualResult.totalElements());
        assertEquals(0, actualResult.pageNumber());
        assertEquals(10, actualResult.size());
        assertEquals(0, actualResult.totalPages());
    }

    private Page<TransactionDto> createTransactionPageWithEmptyTransactionRecord(Account account) {
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
    }
}
