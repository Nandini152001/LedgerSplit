package com.nandini.ledgersplit.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ExpenseGroup expenseGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_user_id", nullable = false)
    private User paidBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_to_user_id", nullable = false)
    private User paidTo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "settled_at", nullable = false)
    private LocalDateTime settledAt;

    public Settlement() {}

    public Settlement(ExpenseGroup expenseGroup, User paidBy, User paidTo, BigDecimal amount, LocalDateTime settledAt) {
        this.expenseGroup = expenseGroup;
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.amount = amount;
        this.settledAt = settledAt;
    }

    public Long getId() { return id; }
    public ExpenseGroup getExpenseGroup() { return expenseGroup; }
    public User getPaidBy() { return paidBy; }
    public User getPaidTo() { return paidTo; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getSettledAt() { return settledAt; }
}