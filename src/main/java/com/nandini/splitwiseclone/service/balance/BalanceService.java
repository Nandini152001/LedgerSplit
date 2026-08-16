package com.nandini.splitwiseclone.service.balance;
import com.nandini.splitwiseclone.dto.BalanceResponseDTO;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;
import com.nandini.splitwiseclone.repository.ExpenseSplitRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BalanceService {

    private final ExpenseSplitRepository expenseSplitRepository;

    public BalanceService(
            ExpenseSplitRepository expenseSplitRepository) {

        this.expenseSplitRepository = expenseSplitRepository;
    }

    public List<BalanceResponseDTO> calculateGroupBalances(Long groupId) {

        List<ExpenseSplit> splits = expenseSplitRepository.findByExpense_ExpenseGroup_Id(groupId);
        Map<Long, BalanceData> balances = new HashMap<>();
        Map<Long, User> users = new HashMap<>();
        Set<Long> processedExpenses = new HashSet<>();

        for(ExpenseSplit split: splits){
            User payer = split.getExpense().getPaidBy();
            User debtor = split.getUser();

            users.put(payer.getId(), payer);
            users.put(debtor.getId(), debtor);

            BalanceData payerBalance = balances
                    .computeIfAbsent(payer.getId(), id -> new BalanceData());

            BalanceData debtorBalance = balances
                    .computeIfAbsent(debtor.getId(), id -> new BalanceData());

            Long expenseId = split.getExpense().getId();

            if(processedExpenses.add(expenseId)) {
                payerBalance.addPaid(
                        split.getExpense().getAmount()
                );
            }
            debtorBalance.addOwed(
                split.getAmountOwed()
            );
        }

        List<BalanceResponseDTO> responses = new ArrayList<>();

        for(Map.Entry<Long, BalanceData> entry: balances.entrySet()){
            Long userId = entry.getKey();
            BalanceData balanceData = entry.getValue();
            User user = users.get(userId);

            BalanceResponseDTO dto = new BalanceResponseDTO();
            dto.setUserId(user.getId());
            dto.setUserName(user.getName());
            dto.setAmountPaid(balanceData.getAmountPaid());
            dto.setAmountOwed(balanceData.getAmountOwed());
            dto.setNetBalance(balanceData.getNetBalance());

            responses.add(dto);
        }
        return responses;
    }
}
