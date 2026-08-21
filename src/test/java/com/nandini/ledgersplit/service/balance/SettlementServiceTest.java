package com.nandini.ledgersplit.service.balance;

import com.nandini.ledgersplit.dto.BalanceResponseDTO;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

public class SettlementServiceTest {

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private SettlementService settlementService;

    void shouldCalculateSettlementsForMultipleDebtors(){
//        Given
        BalanceResponseDTO srinivas =
                createBalance(1L, "Srinivas", 1765.00, 765.00, 1000);

        BalanceResponseDTO preeti =
                createBalance(2L, "Preeti", 1000, -1800, -800 );

        BalanceResponseDTO rahul =
                createBalance(3L, "Rahul", 2000, -5000, -3000);

        when(balanceService.calculateGroupBalances(1L)).thenReturn(List.of(srinivas, preeti, rahul));


    }

    private BalanceResponseDTO createBalance(
            Long userID,
            String userName,
            double amountPaid,
            double amountOwed,
            double netBalance
    ){
            BalanceResponseDTO balance = new BalanceResponseDTO();

            balance.setUserId(userID);
            balance.setUserName(userName);
            balance.setAmountPaid(BigDecimal.valueOf(amountPaid));
            balance.setAmountOwed(BigDecimal.valueOf(amountOwed));
            balance.setNetBalance(BigDecimal.valueOf(netBalance));

            return balance;

    }
}
