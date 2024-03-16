package com.example.usermicroservice.repository;

import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Modifying
    @Query("update User set role = :role where email = :email")
    void updateUserRole(@Param("email") String email, @Param("role") Role role);
    public User findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    @Query("select u from User u")
    public User findUserBy(Long id);


}
