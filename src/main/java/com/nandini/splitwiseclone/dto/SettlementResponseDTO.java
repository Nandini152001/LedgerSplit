package com.nandini.splitwiseclone.dto;

import java.math.BigDecimal;

public record SettlementResponseDTO (
         Long fromUserId,
         Long fromUserName,
         Long toUserId,
         Long toUserName,
         BigDecimal amountPaid
){
}
