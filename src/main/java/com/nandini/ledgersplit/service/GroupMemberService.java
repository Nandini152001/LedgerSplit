package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.AddGroupMemberRequestDTO;
import com.nandini.ledgersplit.dto.GroupMemberResponseDTO;
import com.nandini.ledgersplit.enums.GroupRole;
import com.nandini.ledgersplit.exception.GroupNotFoundException;
import com.nandini.ledgersplit.exception.GroupMemberAlreadyExistsException;
import com.nandini.ledgersplit.exception.GroupMemberNotFoundException;
import com.nandini.ledgersplit.exception.UserNotFoundException;
import com.nandini.ledgersplit.model.ExpenseGroup;
import com.nandini.ledgersplit.model.GroupMember;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.ExpenseGroupRepository;
import com.nandini.ledgersplit.repository.GroupMemberRepository;
import com.nandini.ledgersplit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final UserRepository userRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, ExpenseGroupRepository expenseGroupRepository, UserRepository userRepository) {
        this.groupMemberRepository = groupMemberRepository;
        this.expenseGroupRepository = expenseGroupRepository;
        this.userRepository = userRepository;
    }

    public GroupMemberResponseDTO addMemberToGroup(Long groupId, AddGroupMemberRequestDTO userToAdd) {

        ExpenseGroup expenseGroup = expenseGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        User user = userRepository.findById(userToAdd.getUserId()).orElseThrow(() -> new UserNotFoundException(userToAdd.getUserId()));

        boolean alreadyMember = groupMemberRepository.existsByExpenseGroup_idAndUser_id(groupId, userToAdd.getUserId());

        if (alreadyMember) {
            throw new GroupMemberAlreadyExistsException(groupId, userToAdd.getUserId());
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setExpenseGroup(expenseGroup);
        groupMember.setUser(user);
        groupMember.setRole(GroupRole.MEMBER);
        groupMember.setJoinedAt(LocalDateTime.now());

        GroupMember savedMember = groupMemberRepository.save(groupMember);

        return mapToGroupMemberResponseDTO(savedMember);
    }

    public List<GroupMemberResponseDTO> getAllGroupMembersByGroupId(Long groupId){
                expenseGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

                return groupMemberRepository.findByExpenseGroup_Id(groupId)
                        .stream()
                        .map(this:: mapToGroupMemberResponseDTO)
                        .toList();
    }

    public void deleteUserByExpenseGroupId(Long groupId, Long userId){

            expenseGroupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
            userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            GroupMember groupMember = groupMemberRepository.findByExpenseGroup_idAndUser_Id(groupId, userId).orElseThrow(() -> new GroupMemberNotFoundException(groupId, userId));

            groupMemberRepository.delete(groupMember);
    }

        private GroupMemberResponseDTO mapToGroupMemberResponseDTO(GroupMember groupMember){
                return new GroupMemberResponseDTO(
                        groupMember.getId(),
                        groupMember.getUser().getId(),
                        groupMember.getExpenseGroup().getId(),
                        groupMember.getRole(),
                        groupMember.getJoinedAt()
                );
        }

}
