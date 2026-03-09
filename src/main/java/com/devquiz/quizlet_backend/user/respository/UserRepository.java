package com.devquiz.quizlet_backend.user.respository;

import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
//    Optional<User> findByVerificationToken(String token);
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.userId <> :userId ")
       List<User> findAllOtherUsers(Long userId);


    @Query("SELECT u FROM User u WHERE NOT EXISTS (   SELECT 1\n" +
            "    FROM GroupMember gm\n" +
            "    WHERE gm.user.userId = u.userId\n" +
            "    AND gm.group.groupId = :groupId) ")
    List<User> findAllMembersInGroupDB(  Long groupId);

}
