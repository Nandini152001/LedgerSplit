package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class DuplicateParticipantException extends BusinessValidationException{

    public DuplicateParticipantException(){
        super(ErrorCode.DUPLICATE_PARTICIPANT, ("Duplicate participants are not allowed in an expense."));
    }
}
