package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class DuplicateParticipantException extends BusinessValidationException{

    public DuplicateParticipantException(){
        super(ErrorCode.DUPLICATE_PARTICIPANT, ("Duplicate participants are not allowed in an expense."));
    }
}
