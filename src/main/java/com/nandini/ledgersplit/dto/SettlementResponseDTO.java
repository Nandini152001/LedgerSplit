package com.nandini.ledgersplit.dto;

import java.math.BigDecimal;

public record SettlementResponseDTO (
         Long fromUserId,
         String fromUserName,
         Long toUserId,
         String toUserName,
         BigDecimal amountPaid
){
}
