package com.nandini.splitwiseclone.service;

import com.nandini.splitwiseclone.dto.ExpenseRequestDTO;
import com.nandini.splitwiseclone.dto.ExpenseResponseDTO;
import com.nandini.splitwiseclone.dto.ExpenseSplitResponseDTO;
import com.nandini.splitwiseclone.enums.SplitType;
import com.nandini.splitwiseclone.exception.ExpenseGroupNotFoundException;
import com.nandini.splitwiseclone.exception.UserNotFoundException;
import com.nandini.splitwiseclone.exception.UserNotMemberOfGroupException;
import com.nandini.splitwiseclone.model.Expense;
import com.nandini.splitwiseclone.model.ExpenseGroup;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;
import com.nandini.splitwiseclone.repository.*;
import com.nandini.splitwiseclone.service.strategy.SplitStrategy;
import com.nandini.splitwiseclone.service.strategy.SplitStrategyFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SplitStrategyFactory splitStrategyFactory;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            ExpenseSplitRepository expenseSplitRepository,
            ExpenseGroupRepository expenseGroupRepository,
            UserRepository userRepository,
            GroupMemberRepository groupMemberRepository, SplitStrategyFactory splitStrategyFactory
    ){
        this.expenseRepository = expenseRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.expenseGroupRepository = expenseGroupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.splitStrategyFactory = splitStrategyFactory;
    }

    @Transactional
    public ExpenseResponseDTO createExpense(Long groupId, ExpenseRequestDTO requestDTO) {

//        Validate group exists
        ExpenseGroup group = expenseGroupRepository.findById(groupId).orElseThrow(() -> new ExpenseGroupNotFoundException(groupId));

//        Validate payer user exists
        User paidByUser = userRepository.findById(requestDTO.getPaidByUserId()).orElseThrow(() -> new UserNotFoundException(requestDTO.getPaidByUserId()));

//        Validate that the payer is member of the group
        validateUserIsGroupMember(groupId, paidByUser.getId());

//        Validate participants are not duplicate
        validateNoDuplicateParticipants(requestDTO.getParticipantsUserIds());

        List<User> participants = new ArrayList<>();

//        Validate Every participants exists
        for(Long participantsUserId : requestDTO.getParticipantsUserIds()){
            User participant = userRepository.findById(participantsUserId).orElseThrow(() -> new UserNotFoundException(participantsUserId));
//            Validate that participant is member of the group
            validateUserIsGroupMember(groupId, participant.getId());
            participants.add(participant);
        }

        Expense expense = new Expense();
        expense.setDescription(requestDTO.getDescription());
        expense.setAmount(requestDTO.getAmount());
        expense.setSplitType(requestDTO.getSplitType());
        expense.setExpenseGroup(group);
        expense.setPaidBy(paidByUser);
        expense.setCreatedAt(LocalDateTime.now());

        Expense savedExpense = expenseRepository.save(expense);

        SplitStrategy splitStrategy = splitStrategyFactory.getStrategy(requestDTO.getSplitType());

        List<ExpenseSplit> splits = splitStrategy.createSplits(savedExpense, participants, requestDTO.getAmount());
        expenseSplitRepository.saveAll(splits);

        return mapToResponseDTO(savedExpense, splits);

    }

    public List<ExpenseResponseDTO> getExpensesByGroupId(Long groupId){

//        Validate Group Exist
        ExpenseGroup group = expenseGroupRepository.findById(groupId).orElseThrow(() -> new ExpenseGroupNotFoundException(groupId));

        List<Expense> expenses = expenseRepository.findByExpenseGroup_Id(groupId);

        List<ExpenseResponseDTO> responseDTOs = new ArrayList<>();

        for(Expense expense: expenses){
            List<ExpenseSplit> splits = expenseSplitRepository.findByExpense_Id(expense.getId());

            ExpenseResponseDTO responseDTO = mapToResponseDTO(expense, splits);

            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

        private void validateUserIsGroupMember(Long groupId, Long userId){
                boolean isMember = groupMemberRepository.existsByExpenseGroup_idAndUser_id(groupId, userId);

                if(!isMember) {
                    throw new UserNotMemberOfGroupException(userId, groupId);
                }
        }

        public void validateNoDuplicateParticipants(List<Long> participantUserIds){
//            long distinctCount = participantUserIds.stream().distinct().count();
//            if (distinctCount != participantUserIds.size()) {
//                throw new RuntimeException("Duplicate participants found in the expense request");
//            }

            Set<Long> uniqueIds = new HashSet<>(participantUserIds);

            if(uniqueIds.size() != participantUserIds.size()){
                throw new RuntimeException("Duplicate participants are not allowed");
            }
        }

        private ExpenseResponseDTO mapToResponseDTO(Expense expense, List<ExpenseSplit>splits){

            ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

            responseDTO.setExpenseId(expense.getId());
            responseDTO.setDescription(expense.getDescription());
            responseDTO.setAmount(expense.getAmount());
            responseDTO.setSplitType(expense.getSplitType());
            responseDTO.setPaidByUserId(expense.getPaidBy().getId());
            responseDTO.setCreatedAt( expense.getCreatedAt());
            responseDTO.setGroupId(expense.getExpenseGroup().getId());

            List<ExpenseSplitResponseDTO> splitResponseDTOS = new ArrayList<>();
            for(ExpenseSplit split: splits){
                    ExpenseSplitResponseDTO splitResponseDTO = new ExpenseSplitResponseDTO();
                    splitResponseDTO.setUserId(split.getUser().getId());
                    splitResponseDTO.setAmountOwed(split.getAmountOwed());

                    splitResponseDTOS.add(splitResponseDTO);
            }

            responseDTO.setSplits(splitResponseDTOS);

            return responseDTO;
        }

}
