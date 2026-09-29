package com.nandini.ledgersplit.service.strategy;

import com.nandini.ledgersplit.dto.ExpenseRequestDTO;
import com.nandini.ledgersplit.enums.SplitType;
import com.nandini.ledgersplit.model.Expense;
import com.nandini.ledgersplit.model.ExpenseSplit;
import com.nandini.ledgersplit.model.User;

import java.util.List;

public interface SplitStrategy {

    SplitType getSupportedSplitType();

    List<ExpenseSplit> createSplits(
            Expense expense,
            ExpenseRequestDTO requestDTO,
            List<User> participants
    );
}
