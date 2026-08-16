package com.nandini.splitwiseclone.repository;

import com.nandini.splitwiseclone.model.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {

   List<ExpenseSplit> findByExpense_Id(Long expenseId);
   List<ExpenseSplit> findByUser_Id(Long userId);
   List<ExpenseSplit> findByExpense_ExpenseGroup_Id(Long groupId);
}
