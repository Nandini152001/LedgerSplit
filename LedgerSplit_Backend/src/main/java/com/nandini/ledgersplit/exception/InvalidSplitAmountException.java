package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class InvalidSplitAmountException extends BusinessValidationException{

    public InvalidSplitAmountException() {
        super(ErrorCode.INVALID_SPLIT_AMOUNT, ("Total split amount must equal expense amount."));
    }
}
