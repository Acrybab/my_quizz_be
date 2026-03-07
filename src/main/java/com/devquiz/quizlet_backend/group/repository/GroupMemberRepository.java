package com.devquiz.quizlet_backend.group.repository;

import com.devquiz.quizlet_backend.group.dto.Response.GroupMemberReponse;
import com.devquiz.quizlet_backend.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT gm FROM GroupMember gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId")
    GroupMember findByUserIdAndGroupId(Long userId, Long groupId);


    @Query("SELECT gm FROM GroupMember gm WHERE gm.group.groupId = :groupId")
    List<GroupMember> findByGroupId(Long groupId);


}
