package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class GroupNotFoundException extends ResourceNotFoundException{

    public GroupNotFoundException(Long id){
        super(ErrorCode.GROUP_NOT_FOUND, ("Expense group not found with id: " + id));
    }
}
