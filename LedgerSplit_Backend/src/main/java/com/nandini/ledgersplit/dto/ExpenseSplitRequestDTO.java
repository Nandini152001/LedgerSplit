package com.nandini.ledgersplit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ExpenseSplitRequestDTO (

        @NotNull(message = "User id is requied")
        Long userId,

//        @NotNull(message = "Amount owed is required")
        @Positive(message = "Amount owed must be greater than zero")
        BigDecimal amountOwed,

        BigDecimal percentage
){}
