package com.nandini.ledgersplit.dto;

import java.math.BigDecimal;


public class ExpenseSplitResponseDTO {

    private Long userId;
    private BigDecimal amountOwed;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

}
