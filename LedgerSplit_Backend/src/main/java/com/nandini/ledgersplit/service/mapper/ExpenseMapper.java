package com.nandini.ledgersplit.service.mapper;

import com.nandini.ledgersplit.dto.ExpenseResponseDTO;
import com.nandini.ledgersplit.dto.ExpenseSplitResponseDTO;
import com.nandini.ledgersplit.model.Expense;
import com.nandini.ledgersplit.model.ExpenseSplit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseMapper {
    public ExpenseResponseDTO toResponseDTO(
            Expense expense,
            List<ExpenseSplit> splits) {

        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        responseDTO.setExpenseId(expense.getId());
        responseDTO.setDescription(expense.getDescription());
        responseDTO.setAmount(expense.getAmount());
        responseDTO.setSplitType(expense.getSplitType());
        responseDTO.setPaidByUserId(expense.getPaidBy().getId());
        responseDTO.setCreatedAt(expense.getCreatedAt());
        responseDTO.setGroupId(expense.getExpenseGroup().getId());

        List<ExpenseSplitResponseDTO> splitResponseDTOS = new ArrayList<>();

        for (ExpenseSplit split : splits) {

            ExpenseSplitResponseDTO splitDTO =
                    new ExpenseSplitResponseDTO();

            splitDTO.setUserId(split.getUser().getId());
            splitDTO.setAmountOwed(split.getAmountOwed());

            splitResponseDTOS.add(splitDTO);
        }

        responseDTO.setSplits(splitResponseDTOS);

        return responseDTO;
    }

}