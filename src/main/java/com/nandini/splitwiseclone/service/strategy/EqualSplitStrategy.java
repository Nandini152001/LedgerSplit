package com.nandini.splitwiseclone.service.strategy;

import com.nandini.splitwiseclone.dto.ExpenseRequestDTO;
import com.nandini.splitwiseclone.enums.SplitType;
import com.nandini.splitwiseclone.model.Expense;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualSplitStrategy implements SplitStrategy{

    @Override
    public SplitType getSupportedSplitType(){
        return SplitType.EQUAL;
    }

    @Override
    public List<ExpenseSplit> createSplits(Expense expense, ExpenseRequestDTO requestDTO, List<User> participants) {

        int participantCount = participants.size();
        List<ExpenseSplit> splits = new ArrayList<>();

        expense.setAmount(requestDTO.getAmount());

        BigDecimal eachShare = expense.getAmount().divide(BigDecimal.valueOf(participantCount), 2, RoundingMode.DOWN);

        for(User participant: participants){
            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(participant);
            split.setAmountOwed(eachShare);

            splits.add(split);
        }

        return splits;
    }
}
