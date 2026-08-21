package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class GroupNotFoundException extends ResourceNotFoundException{

    public GroupNotFoundException(Long id){
        super(ErrorCode.GROUP_NOT_FOUND, ("Expense group not found with id: " + id));
    }
}
