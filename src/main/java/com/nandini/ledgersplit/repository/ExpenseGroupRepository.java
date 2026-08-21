package com.nandini.ledgersplit.repository;

import com.nandini.ledgersplit.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, Long> {
}
