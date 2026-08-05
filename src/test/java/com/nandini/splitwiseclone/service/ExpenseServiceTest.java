package com.nandini.splitwiseclone.service;


import com.nandini.splitwiseclone.repository.ExpenseRepository;
import com.nandini.splitwiseclone.repository.ExpenseSplitRepository;
import com.nandini.splitwiseclone.service.mapper.ExpenseMapper;
import com.nandini.splitwiseclone.service.strategy.SplitStrategyFactory;
import com.nandini.splitwiseclone.service.validator.ExpenseValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseSplitRepository expenseSplitRepository;

    @Mock
    private SplitStrategyFactory splitStrategyFactory;

    @Mock
    private ExpenseValidationService expenseValidationService;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void shouldCreateExpenseSuccessfully() {
        
    }



}
