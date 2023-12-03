package com.synpulse8.ebanking.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "test-group-id")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: " + record.value());
    }
}
