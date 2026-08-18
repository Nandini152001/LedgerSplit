package com.nandini.splitwiseclone.dto;

import java.math.BigDecimal;

public record SettlementResponseDTO (
         Long fromUserId,
         String fromUserName,
         Long toUserId,
         String toUserName,
         BigDecimal amountPaid
){
}
