package com.nandini.ledgersplit.dto;

import com.nandini.ledgersplit.enums.GroupRole;

import java.time.LocalDateTime;

public class GroupMemberResponseDTO {

    private Long memberId;
    private Long userId;
    private Long groupId;
    private GroupRole role;
    private LocalDateTime joinedAt;

    public GroupMemberResponseDTO(Long memberId, Long userId, Long groupId, GroupRole role, LocalDateTime joinedAt) {
        this.memberId = memberId;
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public GroupRole getRole() {
        return role;
    }

    public void setRole(GroupRole role) {
        this.role = role;
    }

}
