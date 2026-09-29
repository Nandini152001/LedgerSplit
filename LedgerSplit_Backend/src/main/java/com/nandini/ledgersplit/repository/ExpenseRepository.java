package com.nandini.ledgersplit.repository;

import com.nandini.ledgersplit.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByExpenseGroup_Id(Long groupId);
    List<Expense> findByPaidBy_Id(Long userId);
}
