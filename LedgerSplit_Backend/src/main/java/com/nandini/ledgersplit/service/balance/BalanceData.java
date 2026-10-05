package com.nandini.ledgersplit.service.balance;

import java.math.BigDecimal;

public class BalanceData {

    private BigDecimal amountPaid = BigDecimal.ZERO;
    private BigDecimal amountOwed = BigDecimal.ZERO;
    private BigDecimal settlementPaid = BigDecimal.ZERO;
    private BigDecimal settlementReceived = BigDecimal.ZERO;

    public void addPaid(BigDecimal amount) {
        this.amountPaid = this.amountPaid.add(amount);
    }

    public void addOwed(BigDecimal amount) {
        this.amountOwed = this.amountOwed.add(amount);
    }

    public void addSettlementPaid(BigDecimal amount) {
        this.settlementPaid = this.settlementPaid.add(amount);
    }

    public void addSettlementReceived(BigDecimal amount) {
        this.settlementReceived = this.settlementReceived.add(amount);
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public BigDecimal getNetBalance() {
        return amountPaid
                .subtract(amountOwed)
                .add(settlementPaid)
                .subtract(settlementReceived);
    }
}