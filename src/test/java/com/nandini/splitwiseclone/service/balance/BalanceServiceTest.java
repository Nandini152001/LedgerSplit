package com.nandini.splitwiseclone.service.balance;

import com.nandini.splitwiseclone.dto.BalanceResponseDTO;
import com.nandini.splitwiseclone.model.Expense;
import com.nandini.splitwiseclone.model.ExpenseSplit;
import com.nandini.splitwiseclone.model.User;
import com.nandini.splitwiseclone.repository.ExpenseSplitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private ExpenseSplitRepository expenseSplitRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    void shouldCalculateGroupBalancesCorrectly() {

        // --------------------------------------------------
        // 1. Create three test users
        // --------------------------------------------------

        User nandini = new User();
        nandini.setId(1L);
        nandini.setName("Nandini");

        User rahul = new User();
        rahul.setId(2L);
        rahul.setName("Rahul");

        User amit = new User();
        amit.setId(3L);
        amit.setName("Amit");


        // --------------------------------------------------
        // 2. Create one ₹900 expense paid by Nandini
        // --------------------------------------------------

        Expense expense = new Expense();

        expense.setId(100L);
        expense.setAmount(new BigDecimal("900.00"));
        expense.setPaidBy(nandini);


        // --------------------------------------------------
        // 3. Create three expense splits
        // --------------------------------------------------

        ExpenseSplit nandiniSplit = new ExpenseSplit();

        nandiniSplit.setId(1L);
        nandiniSplit.setExpense(expense);
        nandiniSplit.setUser(nandini);
        nandiniSplit.setAmountOwed(new BigDecimal("300.00"));


        ExpenseSplit rahulSplit = new ExpenseSplit();

        rahulSplit.setId(2L);
        rahulSplit.setExpense(expense);
        rahulSplit.setUser(rahul);
        rahulSplit.setAmountOwed(new BigDecimal("300.00"));


        ExpenseSplit amitSplit = new ExpenseSplit();

        amitSplit.setId(3L);
        amitSplit.setExpense(expense);
        amitSplit.setUser(amit);
        amitSplit.setAmountOwed(new BigDecimal("300.00"));


        // --------------------------------------------------
        // 4. Stub repository
        // --------------------------------------------------

        when(expenseSplitRepository.findByExpense_ExpenseGroup_Id(1L))
                .thenReturn(List.of(
                        nandiniSplit,
                        rahulSplit,
                        amitSplit
                ));


        // --------------------------------------------------
        // 5. Call the actual BalanceService
        // --------------------------------------------------

        List<BalanceResponseDTO> balances =
                balanceService.calculateGroupBalances(1L);


        // --------------------------------------------------
        // 6. Verify three users are present
        // --------------------------------------------------

        assertEquals(3, balances.size());


        // --------------------------------------------------
        // 7. Get Nandini's balance
        // --------------------------------------------------

        BalanceResponseDTO nandiniBalance =
                balances.stream()
                        .filter(balance ->
                                balance.getUserId().equals(1L))
                        .findFirst()
                        .orElseThrow();


        // --------------------------------------------------
        // 8. Get Rahul's balance
        // --------------------------------------------------

        BalanceResponseDTO rahulBalance =
                balances.stream()
                        .filter(balance ->
                                balance.getUserId().equals(2L))
                        .findFirst()
                        .orElseThrow();


        // --------------------------------------------------
        // 9. Get Amit's balance
        // --------------------------------------------------

        BalanceResponseDTO amitBalance =
                balances.stream()
                        .filter(balance ->
                                balance.getUserId().equals(3L))
                        .findFirst()
                        .orElseThrow();


        // ==================================================
        // Nandini
        //
        // Paid = ₹900
        // Owed = ₹300
        // Net  = ₹600
        // ==================================================

        assertEquals(
                0,
                nandiniBalance.getAmountPaid()
                        .compareTo(new BigDecimal("900.00"))
        );

        assertEquals(
                0,
                nandiniBalance.getAmountOwed()
                        .compareTo(new BigDecimal("300.00"))
        );

        assertEquals(
                0,
                nandiniBalance.getNetBalance()
                        .compareTo(new BigDecimal("600.00"))
        );


        // ==================================================
        // Rahul
        //
        // Paid = ₹0
        // Owed = ₹300
        // Net  = -₹300
        // ==================================================

        assertEquals(
                0,
                rahulBalance.getAmountPaid()
                        .compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                0,
                rahulBalance.getAmountOwed()
                        .compareTo(new BigDecimal("300.00"))
        );

        assertEquals(
                0,
                rahulBalance.getNetBalance()
                        .compareTo(new BigDecimal("-300.00"))
        );


        // ==================================================
        // Amit
        //
        // Paid = ₹0
        // Owed = ₹300
        // Net  = -₹300
        // ==================================================

        assertEquals(
                0,
                amitBalance.getAmountPaid()
                        .compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                0,
                amitBalance.getAmountOwed()
                        .compareTo(new BigDecimal("300.00"))
        );

        assertEquals(
                0,
                amitBalance.getNetBalance()
                        .compareTo(new BigDecimal("-300.00"))
        );
    }
}