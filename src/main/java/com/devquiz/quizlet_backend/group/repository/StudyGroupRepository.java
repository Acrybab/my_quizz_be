package com.devquiz.quizlet_backend.group.repository;

import com.devquiz.quizlet_backend.group.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    // @Query("SELECT sg FROM StudyGroup sg Where")
//    Page<StudyGroup> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT sg FROM  StudyGroup sg Where sg.admin.userId = :userId")
    List<StudyGroup> findByAdminUserId(Long userId);

     @Query("SELECT sg FROM StudyGroup sg JOIN GroupMember gm  ON sg.groupId =  gm.group.groupId  WHERE  gm.user.userId= :userId")
    List<StudyGroup> findByMemberUserId(Long userId);
}
