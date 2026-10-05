package com.nandini.ledgersplit.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpenseEventConsumer {

    @KafkaListener(topics = "expense-created", groupId = "ledgersplit")
    public void onExpenseCreated(String message) {
        log.info("Received expense-created event: {}", message);
    }
}