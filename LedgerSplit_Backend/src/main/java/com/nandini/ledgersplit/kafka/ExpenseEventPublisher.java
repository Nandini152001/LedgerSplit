package com.nandini.ledgersplit.kafka;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpenseEventPublisher {

    public final KafkaTemplate<String, String> kafkaTemplate;

    public ExpenseEventPublisher(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishExpenseCreated(Long groupId, Long expenseId){
        String message = String.format(
                "{\"groupId\":%d,\"expenseId\":%d}", groupId, expenseId
        );
        kafkaTemplate.send("expense-created", groupId.toString(), message);
        log.info("Published expense-created event: {}", message);
    }


}
