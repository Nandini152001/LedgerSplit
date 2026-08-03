package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class InvalidSplitCountException extends BusinessValidationException{

    public InvalidSplitCountException() {
        super(ErrorCode.INVALID_SPLIT_COUNT, ("Number of splits must be equal to number of participants."));
    }
}
