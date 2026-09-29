package com.nandini.ledgersplit.exception;

import com.nandini.ledgersplit.enums.ErrorCode;

public class UserNotMemberOfGroupException extends ForbiddenOperationException{
    public UserNotMemberOfGroupException(Long userId, Long groupId){
        super(ErrorCode.USER_NOT_MEMBER_OF_GROUP, ("User with userId: " + userId + " is not a member of groupId: " + groupId));
    }
}
