package com.nandini.ledgersplit.service.strategy;

import com.nandini.ledgersplit.dto.ExpenseRequestDTO;
import com.nandini.ledgersplit.dto.ExpenseSplitRequestDTO;
import com.nandini.ledgersplit.enums.SplitType;
import com.nandini.ledgersplit.model.Expense;
import com.nandini.ledgersplit.model.ExpenseSplit;
import com.nandini.ledgersplit.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExactSplitStrategy implements SplitStrategy{

    @Override
    public SplitType getSupportedSplitType() {
        return SplitType.EXACT;
    }

//    Business Validations:
//    Validation 1: No. of splits = No. of participants
    public void validateSplitCount(ExpenseRequestDTO requestDTO, List<User> participants){
        if(requestDTO.getSplits().size() != participants.size()){
            throw new IllegalArgumentException("Number of splits must match the number of participants.");
        }
    }

// Validation 2: Sum of all splits money = Total Expense Amount
    public void validateTotalSplitMoney(ExpenseRequestDTO requestDTO){
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        for(ExpenseSplitRequestDTO split: requestDTO.getSplits()){
//                totalAmount = totalAmount.add(split.amountOwed());
//        }

        BigDecimal totalAmount = requestDTO.getSplits()
                .stream()
                .map(ExpenseSplitRequestDTO::amountOwed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(requestDTO.getAmount().compareTo(totalAmount) != 0){
            throw new IllegalArgumentException("Wrong Money Input");
        }
    }

//    Validation 3: Every Split user must be a part of participants list
    public void validateEachParticipantHasExactlyOneSplit(ExpenseRequestDTO requestDTO, List<User> participants){
            for(User participant: participants){
                long count = requestDTO.getSplits().stream()
                        .filter(split -> split.userId().equals(participant.getId()))
                        .count();

                if(count != 1){
                    throw new IllegalArgumentException(
                            "Each participant must have exactly one split. Problem with user: " + participant.getId()
                    );
                }
            }
    }

    @Override
    public List<ExpenseSplit> createSplits(Expense expense, ExpenseRequestDTO requestDTO, List<User> participants) {

//        Implementing Validation 1:
        validateSplitCount(requestDTO, participants);
        List<ExpenseSplit> splits = new ArrayList<>();
        for(ExpenseSplitRequestDTO splitRequest: requestDTO.getSplits()){
                    Long userId = splitRequest.userId();
                    User participant = participants
                                        .stream()
                                        .filter(user -> user.getId().equals(userId))
                                        .findFirst()
                                        .orElseThrow(
                                                () -> new IllegalArgumentException( "Participant not found: " + splitRequest.userId())
                                        );
                    ExpenseSplit split = new ExpenseSplit();
                    split.setExpense(expense);
                    split.setAmountOwed(splitRequest.amountOwed());
                    split.setUser(participant);
                    splits.add(split);
        }
        return splits;
    }
}
