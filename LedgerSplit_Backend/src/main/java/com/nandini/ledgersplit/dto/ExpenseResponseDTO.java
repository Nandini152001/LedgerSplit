package com.nandini.ledgersplit.dto;

import com.nandini.ledgersplit.enums.SplitType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseResponseDTO {

    private Long expenseId;
    private String description;
    private BigDecimal amount;
    private Long groupId;
    private Long paidByUserId;
    private SplitType splitType;
    private LocalDateTime createdAt;
    private List<ExpenseSplitResponseDTO> splits;

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(Long paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ExpenseSplitResponseDTO> getSplits() {
        return splits;
    }

    public void setSplits(List<ExpenseSplitResponseDTO> splits) {
        this.splits = splits;
    }
}
