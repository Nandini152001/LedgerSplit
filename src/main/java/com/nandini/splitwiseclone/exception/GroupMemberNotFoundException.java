package com.nandini.splitwiseclone.exception;

import com.nandini.splitwiseclone.dto.ErrorResponseDTO;
import com.nandini.splitwiseclone.enums.ErrorCode;

public class GroupMemberNotFoundException extends ResourceNotFoundException{

    public GroupMemberNotFoundException(Long groupId,Long userId){
        super(ErrorCode.GROUP_MEMBER_NOT_FOUND, ("User with id: " + userId + " is not a user of groupId: " + groupId));
    }
}
