package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class SplitAmountMismatchException extends ForbiddenOperationException{

    public SplitAmountMismatchException(){
        super(ErrorCode.INVALID_EXPENSE_SPLIT, ("sum of split amounts doesn't equal expense amount"));
    }
}
