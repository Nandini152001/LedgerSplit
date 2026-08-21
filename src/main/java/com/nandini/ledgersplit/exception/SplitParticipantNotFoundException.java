package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class SplitParticipantNotFoundException extends BusinessValidationException{

    public SplitParticipantNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND, ("This user is not a participant."));
    }
}
