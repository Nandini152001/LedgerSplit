package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class InvalidSplitAmountException extends BusinessValidationException{

    public InvalidSplitAmountException() {
        super(ErrorCode.INVALID_SPLIT_AMOUNT, ("Total split amount must equal expense amount."));
    }
}
