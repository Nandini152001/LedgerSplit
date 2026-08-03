package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class UserNotFoundException extends ResourceNotFoundException{

    public UserNotFoundException(Long id){
        super (ErrorCode.USER_NOT_FOUND, ("User not found with id: " + id));
    }
}
