package com.nandini.splitwiseclone.service.strategy;

import com.nandini.splitwiseclone.enums.SplitType;
import com.nandini.splitwiseclone.model.Expense;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface SplitStrategy {

    SplitType getSupportedSplitType();

    List<ExpenseSplit> createSplits(
            Expense expense,
            List<User> participants,
            BigDecimal totalAmount
    );
}
