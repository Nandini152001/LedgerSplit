package com.nandini.splitwiseclone.service;

import com.nandini.splitwiseclone.dto.ExpenseRequestDTO;
import com.nandini.splitwiseclone.dto.ExpenseResponseDTO;
import com.nandini.splitwiseclone.enums.SplitType;
import com.nandini.splitwiseclone.model.Expense;
import com.nandini.splitwiseclone.model.ExpenseGroup;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;
import com.nandini.splitwiseclone.repository.ExpenseRepository;
import com.nandini.splitwiseclone.repository.ExpenseSplitRepository;
import com.nandini.splitwiseclone.service.mapper.ExpenseMapper;
import com.nandini.splitwiseclone.service.strategy.SplitStrategy;
import com.nandini.splitwiseclone.service.strategy.SplitStrategyFactory;
import com.nandini.splitwiseclone.service.validator.ExpenseValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

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

        // =========================
        // ARRANGE
        // =========================

        Long groupId = 1L;

        // Group
        ExpenseGroup group = new ExpenseGroup();
        group.setId(groupId);


        // Request DTO
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO();

        requestDTO.setDescription("Dinner");
        requestDTO.setAmount(new BigDecimal("1200.00"));
        requestDTO.setPaidByUserId(10L);
        requestDTO.setSplitType(SplitType.EQUAL);

        requestDTO.setParticipantsUserIds(
                List.of(10L, 20L, 30L)
        );


        // Payer
        User paidByUser = new User();
        paidByUser.setId(10L);


        // Participants
        User participant1 = new User();
        participant1.setId(10L);

        User participant2 = new User();
        participant2.setId(20L);

        User participant3 = new User();
        participant3.setId(30L);

        List<User> participants = List.of(
                participant1,
                participant2,
                participant3
        );


        // =========================
        // STUB VALIDATION SERVICE
        // =========================

        when(expenseValidationService
                .validateAndGetGroup(groupId))
                .thenReturn(group);


        when(expenseValidationService
                .validateAndGetUser(
                        requestDTO.getPaidByUserId()))
                .thenReturn(paidByUser);


        when(expenseValidationService
                .validateParticipants(
                        groupId,
                        requestDTO.getParticipantsUserIds()))
                .thenReturn(participants);


        // =========================
        // STUB EXPENSE REPOSITORY
        // =========================

        Expense savedExpense = new Expense();

        savedExpense.setId(100L);
        savedExpense.setExpenseGroup(group);
        savedExpense.setDescription(
                requestDTO.getDescription()
        );
        savedExpense.setAmount(
                requestDTO.getAmount()
        );
        savedExpense.setSplitType(
                requestDTO.getSplitType()
        );
        savedExpense.setPaidBy(paidByUser);


        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(savedExpense);


        // =========================
        // STUB STRATEGY FACTORY
        // =========================

        SplitStrategy splitStrategy =
                mock(SplitStrategy.class);

        when(splitStrategyFactory.getStrategy(
                requestDTO.getSplitType()))
                .thenReturn(splitStrategy);


        // =========================
        // STUB SPLIT STRATEGY
        // =========================

        List<ExpenseSplit> splits = List.of();

        when(splitStrategy.createSplits(
                savedExpense,
                requestDTO,
                participants))
                .thenReturn(splits);


        // =========================
        // STUB MAPPER
        // =========================

        ExpenseResponseDTO responseDTO =
                new ExpenseResponseDTO();

        responseDTO.setExpenseId(100L);
        responseDTO.setDescription("Dinner");
        responseDTO.setAmount(
                new BigDecimal("1200.00")
        );
        responseDTO.setGroupId(groupId);
        responseDTO.setPaidByUserId(10L);
        responseDTO.setSplitType(SplitType.EQUAL);
        responseDTO.setSplits(List.of());


        when(expenseMapper.toResponseDTO(
                savedExpense,
                splits))
                .thenReturn(responseDTO);


        // =========================
        // ACT
        // =========================

        ExpenseResponseDTO result =
                expenseService.createExpense(
                        groupId,
                        requestDTO
                );


        // =========================
        // ASSERT
        // =========================

        assertSame(responseDTO, result);


        // =========================
        // VERIFY
        // =========================

        verify(expenseValidationService)
                .validateAndGetGroup(groupId);

        verify(expenseValidationService)
                .validateAndGetUser(
                        requestDTO.getPaidByUserId()
                );

        verify(expenseValidationService)
                .validateUserIsGroupMember(
                        groupId,
                        paidByUser.getId()
                );

        verify(expenseValidationService)
                .validateParticipants(
                        groupId,
                        requestDTO.getParticipantsUserIds()
                );

        verify(expenseRepository)
                .save(any(Expense.class));

        verify(splitStrategyFactory)
                .getStrategy(
                        requestDTO.getSplitType()
                );

        verify(splitStrategy)
                .createSplits(
                        savedExpense,
                        requestDTO,
                        participants
                );

        verify(expenseSplitRepository)
                .saveAll(splits);

        verify(expenseMapper)
                .toResponseDTO(
                        savedExpense,
                        splits
                );
    }
}