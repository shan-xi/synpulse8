package com.synpulse8.ebanking.transactions.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebanking.account.repo.AccountRepository;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import com.synpulse8.ebanking.transactions.dto.TransactionData;
import com.synpulse8.ebanking.transactions.entity.Transaction;
import com.synpulse8.ebanking.transactions.repo.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
            id = "transaction-recordListener-topic",
            topics = "transaction-recordListener-topic",
            groupId = "group-synpulse8",
            containerGroup = "group-synpulse8",
            concurrency = "3")
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void transactionRecordHandler(ConsumerRecord<String, String> record) throws JsonProcessingException {
        var transactionData = objectMapper.readValue(record.value(), TransactionData.class);
        var account = accountRepository.findByUid(transactionData.getAccountUid()).orElseThrow(UserDataNotFoundRuntimeException::new);
        var transaction = Transaction.builder()
                .transactionId(record.key())
                .currency(transactionData.getCurrency())
                .amount(transactionData.getAmount())
                .iban(transactionData.getIban())
                .valueDate(transactionData.getValueDate())
                .description(transactionData.getDescription())
                .account(account)
                .build();
        transactionRepository.save(transaction);
    }
}
