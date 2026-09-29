package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class UserNotFoundException extends ResourceNotFoundException{

    public UserNotFoundException(Long id){
        super (ErrorCode.USER_NOT_FOUND, ("User not found with id: " + id));
    }
}
