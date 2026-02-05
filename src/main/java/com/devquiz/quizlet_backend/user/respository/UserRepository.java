package com.devquiz.quizlet_backend.user.respository;

import com.devquiz.quizlet_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
//    Optional<User> findByVerificationToken(String token);
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

}
