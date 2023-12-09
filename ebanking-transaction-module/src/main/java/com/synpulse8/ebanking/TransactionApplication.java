package com.synpulse8.ebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableKafka
@EnableAspectJAutoProxy
@EnableTransactionManagement
@SpringBootApplication
public class TransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

}
