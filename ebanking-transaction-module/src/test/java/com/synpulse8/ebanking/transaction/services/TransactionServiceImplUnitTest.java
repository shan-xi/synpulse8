package com.synpulse8.ebanking.transaction.services;

import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.transaction.repo.TransactionRepository;
import com.synpulse8.ebanking.enums.BalanceChange;
import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.security.PrincipleUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;

class TransactionServiceImplUnitTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PrincipleUtils principleUtils;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //    @Test
    void getTransactionList_Successful() {
//        // Arrange
//        var uid = "P-0123456789";
//        var baseCurrency = Currency.USD;
//        var searchDto = new TransactionSearchDto(
//                LocalDate.now().minusDays(7),
//                LocalDate.now(),
//                0,
//                10,
//                baseCurrency
//        );
//
//        var account = new Account();
//        account.setUid(uid);
//        account.setCurrency(Currency.EUR);
//        var transactionPage = createTransactionPage(account);
//
//        var client = new Client();
//        client.setUid(uid);
//        client.setAccountList(Collections.singletonList(account));
//
//        when(clientRepository.findByUid(uid)).thenReturn(Optional.of(client));
//        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(transactionPage);
//        when(exchangeRateService.getExchangeRates(baseCurrency.name())).thenReturn(Collections.singletonMap("EUR", 1.2));
//        when(principleUtils.getUid()).thenReturn(uid);
//
//        // Act
//        var actualResult = transactionService.getTransactionList(searchDto);
//
//        // Assert
//        assertEquals(1, actualResult.transactionResList().size());
//        assertEquals(1, actualResult.totalElements());
//        assertEquals(0, actualResult.pageNumber());
//        assertEquals(10, actualResult.size());
//        assertEquals(1, actualResult.totalPages());
    }

    private Page<Transaction> createTransactionPage(Account account) {
        var transaction = new Transaction();
        transaction.setTransactionId(String.valueOf(1L));
        transaction.setCurrency(Currency.EUR);
        transaction.setAmount(100.0);
        transaction.setBalanceChange(BalanceChange.CREDIT);
        transaction.setIban("IBAN123");
        transaction.setValueDate(LocalDate.now());
        transaction.setDescription("Test Transaction");
        transaction.setAccount(account);

        return new PageImpl<>(Collections.singletonList(transaction), PageRequest.of(0, 10), 1);
    }

    //    @Test
    void getTransactionList_Successful_EmptyTransactionRecord() {
//        // Arrange
//        var uid = "P-0123456789";
//        var baseCurrency = Currency.USD;
//        var searchDto = new TransactionSearchDto(
//                LocalDate.now().minusDays(7),
//                LocalDate.now(),
//                0,
//                10,
//                baseCurrency
//        );
//
//        var account = new Account();
//        account.setUid(uid);
//        account.setCurrency(Currency.EUR);
//        var transactionPage = createTransactionPageWithEmptyTransactionRecord(account);
//
//        var client = new Client();
//        client.setUid(uid);
//        client.setAccountList(Collections.singletonList(account));
//
//        when(clientRepository.findByUid(uid)).thenReturn(Optional.of(client));
//        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(transactionPage);
//        when(exchangeRateService.getExchangeRates(baseCurrency.name())).thenReturn(Collections.singletonMap("EUR", 1.2));
//        when(principleUtils.getUid()).thenReturn(uid);
//
//        // Act
//        var actualResult = transactionService.getTransactionList(searchDto);
//
//        // Assert
//        assertEquals(0, actualResult.transactionResList().size());
//        assertEquals(0, actualResult.totalElements());
//        assertEquals(0, actualResult.pageNumber());
//        assertEquals(10, actualResult.size());
//        assertEquals(0, actualResult.totalPages());
    }

    private Page<Transaction> createTransactionPageWithEmptyTransactionRecord(Account account) {
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
    }
}
