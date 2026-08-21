package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.ExpenseRequestDTO;
import com.nandini.ledgersplit.dto.ExpenseResponseDTO;
import com.nandini.ledgersplit.model.Expense;
import com.nandini.ledgersplit.model.ExpenseGroup;
import com.nandini.ledgersplit.model.ExpenseSplit;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.ExpenseRepository;
import com.nandini.ledgersplit.repository.ExpenseSplitRepository;
import com.nandini.ledgersplit.service.mapper.ExpenseMapper;
import com.nandini.ledgersplit.service.strategy.SplitStrategy;
import com.nandini.ledgersplit.service.strategy.SplitStrategyFactory;
import com.nandini.ledgersplit.service.validator.ExpenseValidationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final SplitStrategyFactory splitStrategyFactory;
    private final ExpenseValidationService expenseValidationService;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseSplitRepository expenseSplitRepository,
            SplitStrategyFactory splitStrategyFactory,
            ExpenseValidationService expenseValidationService,
            ExpenseMapper expenseMapper) {

        this.expenseRepository = expenseRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.splitStrategyFactory = splitStrategyFactory;
        this.expenseValidationService = expenseValidationService;
        this.expenseMapper = expenseMapper;
    }

    @Transactional
    public ExpenseResponseDTO createExpense(Long groupId,
                                            ExpenseRequestDTO requestDTO) {

        // Validate group
        ExpenseGroup group =
                expenseValidationService.validateAndGetGroup(groupId);

        // Validate payer
        User paidByUser =
                expenseValidationService.validateAndGetUser(
                        requestDTO.getPaidByUserId());

        expenseValidationService.validateUserIsGroupMember(
                groupId,
                paidByUser.getId());

        // Validate all participants
        List<User> participants =
                expenseValidationService.validateParticipants(
                        groupId,
                        requestDTO.getParticipantsUserIds());

        // Create Expense
        Expense expense = new Expense();
        expense.setExpenseGroup(group);
        expense.setDescription(requestDTO.getDescription());
        expense.setAmount(requestDTO.getAmount());
        expense.setSplitType(requestDTO.getSplitType());
        expense.setPaidBy(paidByUser);
        expense.setCreatedAt(LocalDateTime.now());

        Expense savedExpense = expenseRepository.save(expense);

        // Create Splits
        SplitStrategy splitStrategy =
                splitStrategyFactory.getStrategy(requestDTO.getSplitType());

        List<ExpenseSplit> splits =
                splitStrategy.createSplits(
                        savedExpense,
                        requestDTO,
                        participants);

        expenseSplitRepository.saveAll(splits);

        return expenseMapper.toResponseDTO(savedExpense, splits);
    }

    public List<ExpenseResponseDTO> getExpensesByGroupId(Long groupId) {

        // Validate group exists
        expenseValidationService.validateAndGetGroup(groupId);

        List<Expense> expenses =
                expenseRepository.findByExpenseGroup_Id(groupId);

        List<ExpenseResponseDTO> responseDTOs = new ArrayList<>();

        for (Expense expense : expenses) {

            List<ExpenseSplit> splits =
                    expenseSplitRepository.findByExpense_Id(expense.getId());

            responseDTOs.add(
                    expenseMapper.toResponseDTO(expense, splits)
            );
        }

        return responseDTOs;
    }
}