package com.nandini.splitwiseclone.exception;

public class UserNotMemberOfGroupException extends RuntimeException{
    public UserNotMemberOfGroupException(Long userId, Long groupId){
        super("User with userId: " + userId + " is not a member of groupId: " + groupId);
    }
}
