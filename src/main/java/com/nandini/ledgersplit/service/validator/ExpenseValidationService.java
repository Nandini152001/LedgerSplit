package com.nandini.ledgersplit.service.validator;

import com.nandini.ledgersplit.exception.DuplicateParticipantException;
import com.nandini.ledgersplit.exception.GroupNotFoundException;
import com.nandini.ledgersplit.exception.UserNotFoundException;
import com.nandini.ledgersplit.exception.UserNotMemberOfGroupException;
import com.nandini.ledgersplit.model.ExpenseGroup;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.ExpenseGroupRepository;
import com.nandini.ledgersplit.repository.GroupMemberRepository;
import com.nandini.ledgersplit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseValidationService {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public ExpenseValidationService(
            ExpenseGroupRepository expenseGroupRepository,
            UserRepository userRepository,
            GroupMemberRepository groupMemberRepository) {

        this.expenseGroupRepository = expenseGroupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * Validate group exists.
     */
    public ExpenseGroup validateAndGetGroup(Long groupId) {

        return expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    /**
     * Validate user exists.
     */
    public User validateAndGetUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * Validate user belongs to the given group.
     */
    public void validateUserIsGroupMember(Long groupId, Long userId) {

        boolean isMember = groupMemberRepository
                .existsByExpenseGroup_idAndUser_Id(groupId, userId);

        if (!isMember) {
            throw new UserNotMemberOfGroupException(userId, groupId);
        }
    }

    /**
     * Validate participant list contains no duplicates.
     */
    public void validateNoDuplicateParticipants(List<Long> participantIds) {

        Set<Long> uniqueIds = new HashSet<>(participantIds);

        if (uniqueIds.size() != participantIds.size()) {
            throw new DuplicateParticipantException();
        }
    }

    /**
     * Validate every participant:
     * - exists
     * - belongs to the group
     *
     * Returns the validated User objects.
     */
    public List<User> validateParticipants(
            Long groupId,
            List<Long> participantIds) {

        validateNoDuplicateParticipants(participantIds);

        List<User> participants = new ArrayList<>();

        for (Long participantId : participantIds) {

            User participant = validateAndGetUser(participantId);

            validateUserIsGroupMember(groupId, participantId);

            participants.add(participant);
        }

        return participants;
    }
}