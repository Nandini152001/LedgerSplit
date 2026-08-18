package com.nandini.splitwiseclone.service.balance;

import com.nandini.splitwiseclone.dto.BalanceResponseDTO;
import com.nandini.splitwiseclone.dto.SettlementResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettlementService {

    private final BalanceService balanceService;

    public SettlementService(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    public List<SettlementResponseDTO> calculateSettlements(Long groupId) {

        List<BalanceResponseDTO> balances =
                balanceService.calculateGroupBalances(groupId);

        List<RemainingBalance> debtors = new ArrayList<>();
        List<RemainingBalance> creditors = new ArrayList<>();

        // Separate users into debtors and creditors
        for (BalanceResponseDTO balance : balances) {

            if (balance.getNetBalance().compareTo(BigDecimal.ZERO) < 0) {

                // Negative balance means user has to pay
                debtors.add(
                        new RemainingBalance(
                                balance,
                                balance.getNetBalance().abs()
                        )
                );

            } else if (balance.getNetBalance().compareTo(BigDecimal.ZERO) > 0) {

                // Positive balance means user has to receive
                creditors.add(
                        new RemainingBalance(
                                balance,
                                balance.getNetBalance()
                        )
                );
            }
        }

        List<SettlementResponseDTO> settlements = new ArrayList<>();

        int debtorIndex = 0;
        int creditorIndex = 0;

        while (debtorIndex < debtors.size()
                && creditorIndex < creditors.size()) {

            RemainingBalance debtor = debtors.get(debtorIndex);
            RemainingBalance creditor = creditors.get(creditorIndex);

            // Settle the maximum possible amount
            BigDecimal settlementAmount =
                    debtor.getAmount().min(creditor.getAmount());

            BalanceResponseDTO debtorBalance = debtor.getBalance();
            BalanceResponseDTO creditorBalance = creditor.getBalance();

            // Create settlement
            settlements.add(
                    new SettlementResponseDTO(
                            debtorBalance.getUserId(),
                            debtorBalance.getUserName(),
                            creditorBalance.getUserId(),
                            creditorBalance.getUserName(),
                            settlementAmount
                    )
            );

            // Reduce remaining balances
            debtor.reduceAmount(settlementAmount);
            creditor.reduceAmount(settlementAmount);

            // Move to next debtor if current debtor is fully settled
            if (debtor.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                debtorIndex++;
            }

            // Move to next creditor if current creditor is fully settled
            if (creditor.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                creditorIndex++;
            }
        }

        return settlements;
    }

    /**
     * Temporary state used during settlement calculation.
     *
     * BalanceResponseDTO represents the calculated balance,
     * while this class keeps track of the remaining amount
     * during the settlement process.
     */
    private static class RemainingBalance {

        private final BalanceResponseDTO balance;
        private BigDecimal amount;

        public RemainingBalance(
                BalanceResponseDTO balance,
                BigDecimal amount) {

            this.balance = balance;
            this.amount = amount;
        }

        public BalanceResponseDTO getBalance() {
            return balance;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void reduceAmount(BigDecimal amount) {
            this.amount = this.amount.subtract(amount);
        }
    }
}