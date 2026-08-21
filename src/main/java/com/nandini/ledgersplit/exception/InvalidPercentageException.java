package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class InvalidPercentageException extends BusinessValidationException{

    public InvalidPercentageException() {
        super(ErrorCode.INVALID_PERCENTAGE, ("Total percentage of splits must be 100%."));
    }
}
