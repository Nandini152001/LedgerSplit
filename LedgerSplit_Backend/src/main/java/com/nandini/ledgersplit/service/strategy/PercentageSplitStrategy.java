package com.nandini.ledgersplit.service.strategy;

import com.nandini.ledgersplit.dto.ExpenseRequestDTO;
import com.nandini.ledgersplit.dto.ExpenseSplitRequestDTO;
import com.nandini.ledgersplit.enums.SplitType;
import com.nandini.ledgersplit.model.Expense;
import com.nandini.ledgersplit.model.ExpenseSplit;
import com.nandini.ledgersplit.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public SplitType getSupportedSplitType() {
        return SplitType.PERCENTAGE;
    }

    @Override
    public List<ExpenseSplit> createSplits(
            Expense expense,
            ExpenseRequestDTO requestDTO,
            List<User> participants) {

        validateSplitCount(requestDTO, participants);
        validateParticipantMapping(requestDTO, participants);
        validatePercentagePresent(requestDTO);
        validatePercentagePositive(requestDTO);
        validatePercentageSum(requestDTO);

        List<ExpenseSplit> splits = new ArrayList<>();

        for (ExpenseSplitRequestDTO splitRequest : requestDTO.getSplits()) {

            User participant = participants.stream()
                    .filter(user -> user.getId().equals(splitRequest.userId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException("Participant not found: " + splitRequest.userId()));

            BigDecimal amountOwed = expense.getAmount()
                    .multiply(splitRequest.percentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(participant);
            split.setAmountOwed(amountOwed);

            splits.add(split);
        }

        return splits;
    }

    private void validateSplitCount(
            ExpenseRequestDTO requestDTO,
            List<User> participants) {

        if (requestDTO.getSplits().size() != participants.size()) {
            throw new IllegalArgumentException(
                    "Number of percentage splits must match participants.");
        }
    }

    private void validateParticipantMapping(
            ExpenseRequestDTO requestDTO,
            List<User> participants) {

        for (User participant : participants) {

            long count = requestDTO.getSplits().stream()
                    .filter(split -> split.userId().equals(participant.getId()))
                    .count();

            if (count != 1) {
                throw new IllegalArgumentException(
                        "Each participant must have exactly one percentage split.");
            }
        }
    }

    private void validatePercentagePresent(
            ExpenseRequestDTO requestDTO) {

        for (ExpenseSplitRequestDTO split : requestDTO.getSplits()) {

            if (split.percentage() == null) {
                throw new IllegalArgumentException(
                        "Percentage is required for every participant.");
            }
        }
    }

    private void validatePercentagePositive(
            ExpenseRequestDTO requestDTO) {

        for (ExpenseSplitRequestDTO split : requestDTO.getSplits()) {

            if (split.percentage().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Percentage must be greater than zero.");
            }
        }
    }

    private void validatePercentageSum(
            ExpenseRequestDTO requestDTO) {

        BigDecimal total = requestDTO.getSplits().stream()
                .map(ExpenseSplitRequestDTO::percentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new IllegalArgumentException(
                    "Total percentage must equal 100.");
        }
    }
}