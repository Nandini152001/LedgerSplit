package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class SplitAmountMismatchException extends ForbiddenOperationException{

    public SplitAmountMismatchException(){
        super(ErrorCode.INVALID_EXPENSE_SPLIT, ("sum of split amounts doesn't equal expense amount"));
    }
}
