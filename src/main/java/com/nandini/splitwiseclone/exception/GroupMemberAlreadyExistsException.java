package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.enums.ErrorCode;

public class GroupMemberAlreadyExistsException extends ConflictException{

    public GroupMemberAlreadyExistsException(Long groupId, Long userId){
        super(ErrorCode.GROUP_MEMBER_ALREADY_EXISTS, ("user with id: " + userId + " is already a member of group with group id: " + groupId));
    }
}
