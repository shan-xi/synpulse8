package com.synpulse8.ebanking.transaction.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.dao.transaction.entity.Transaction;
import com.synpulse8.ebanking.dao.transaction.repo.TransactionRepository;
import com.synpulse8.ebanking.enums.BalanceChange;
import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.test.kafka.dto.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class TransactionRecordListener {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionRecordListener(TransactionRepository transactionRepository,
                                     AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @KafkaListener(
            id = "transaction-record-listener-topic",
            topics = "transaction-record-listener-topic",
            groupId = "group-synpulse8",
            containerGroup = "group-synpulse8",
            concurrency = "3")
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void transactionRecordHandler(ConsumerRecord<String, String> record) throws JsonProcessingException {
        var transactionRecord = objectMapper.readValue(record.value(), TransactionRecord.class);
        var amountCurrencyArr = transactionRecord.amountWithCurrency().split(" ");
        var currency = Currency.fromString(amountCurrencyArr[0]);
        var amountBalanceChange = parseAmountValue(amountCurrencyArr[1]);
        var valueDate = convertToDate(transactionRecord.valueDate());
        var account = accountRepository.findByUidAndCurrency(transactionRecord.accountUid(), currency).orElse(null);
        var transaction = Transaction.builder()
                .transactionId(record.key())
                .currency(currency)
                .amount(amountBalanceChange.amount)
                .balanceChange(amountBalanceChange.balanceChange)
                .iban(transactionRecord.iban())
                .valueDate(valueDate)
                .description(transactionRecord.description())
                .account(account)
                .build();
        transactionRepository.save(transaction);
    }

    private LocalDate convertToDate(String valueDate) {
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(valueDate, formatter);
    }

    private AmountBalanceChange parseAmountValue(String value) {
        var cleanedValue = value.replaceAll("[^0-9.]", "");
        try {
            double parsedValue = Double.parseDouble(cleanedValue);
            var balanceChange = value.endsWith("-") ? BalanceChange.CREDIT : BalanceChange.DEPOSIT;
            return new AmountBalanceChange(parsedValue, balanceChange);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    private record AmountBalanceChange(
            double amount,
            BalanceChange balanceChange) {
    }
}
