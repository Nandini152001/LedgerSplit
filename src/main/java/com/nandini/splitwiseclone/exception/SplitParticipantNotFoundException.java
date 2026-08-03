package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class SplitParticipantNotFoundException extends BusinessValidationException{

    public SplitParticipantNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND, ("This user is not a participant."));
    }
}
