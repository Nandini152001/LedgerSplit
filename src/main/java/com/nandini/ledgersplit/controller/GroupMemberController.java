package com.nandini.ledgersplit.controller;

import com.nandini.ledgersplit.dto.AddGroupMemberRequestDTO;
import com.nandini.ledgersplit.dto.GroupMemberResponseDTO;
import com.nandini.ledgersplit.service.GroupMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group/{groupId}/members")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService){
        this.groupMemberService = groupMemberService;
    }

    @PostMapping
    public ResponseEntity<GroupMemberResponseDTO> addMemberToGroup(@PathVariable Long groupId, @RequestBody AddGroupMemberRequestDTO userToAdd){
        GroupMemberResponseDTO savedMember = groupMemberService.addMemberToGroup(groupId, userToAdd);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    @GetMapping
    public ResponseEntity<List<GroupMemberResponseDTO>> getAllGroupMembers(@PathVariable Long groupId){
        List<GroupMemberResponseDTO> groupMemberResponseDTO =  groupMemberService.getAllGroupMembersByGroupId(groupId);
        return ResponseEntity.ok(groupMemberResponseDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeGroupMemberFromGroup(@PathVariable Long groupId, @PathVariable Long userId){
       groupMemberService.deleteUserByExpenseGroupId(groupId, userId);

       return ResponseEntity.ok("User with Id: " + userId + " removed from group with Id: " + groupId);
    }

}
