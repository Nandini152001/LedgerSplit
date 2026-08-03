package com.nandini.splitwiseclone.repository;

import com.nandini.splitwiseclone.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByExpenseGroup_idAndUser_id(Long groupId, Long userId);

//    Tackling N+1 Query future issues with JOIN FETCH JPQL
    @Query("""
            SELECT gm 
            FROM GroupMember gm
            JOIN FETCH gm.user WHERE gm.expenseGroup.id = :groupId""")
    List<GroupMember> findByExpenseGroup_Id(Long groupId);

     boolean existsByExpenseGroup_idAndUser_Id(Long GroupId, Long UserId);
}
