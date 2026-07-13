package com.nandini.splitwiseclone.dto;

import com.nandini.splitwiseclone.enums.SplitType;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseRequestDTO {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Paid by user id is required")
    private Long paidByUserId;

    @NotNull(message = "Split type is required")
    private SplitType splitType;

//    @NotEmpty(message = "At least one participant is required")
    private List<Long> participantsUserIds;

    private List<ExpenseSplitRequestDTO> splits;

    public List<ExpenseSplitRequestDTO> getSplits() {
        return splits;
    }

    public void setSplits(List<ExpenseSplitRequestDTO> splits) {
        this.splits = splits;
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

    public List<Long> getParticipantsUserIds() {
        return participantsUserIds;
    }

    public void setParticipantsUserIds(List<Long> participantsUserIds) {
        this.participantsUserIds = participantsUserIds;
    }

}
