package com.nandini.splitwiseclone.service.balance;

import java.math.BigDecimal;

public class BalanceData {

    private BigDecimal amountPaid = BigDecimal.ZERO;
    private BigDecimal amountOwed = BigDecimal.ZERO;

    public void addPaid(BigDecimal amount){
            this.amountPaid = this.amountPaid.add(amount);
    }

    public void addOwed(BigDecimal amount){
            this.amountOwed = this.amountOwed.add(amount);
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public BigDecimal getNetBalance(){
        return amountPaid.subtract(amountOwed);
    }
}
