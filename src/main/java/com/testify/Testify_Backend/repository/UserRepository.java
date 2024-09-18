package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    //enable user
    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableUser(String email);

//  verify Organization
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = TRUE where u.id = ?1")
    int verifyUser(int id);

//    reject Organization
    @Transactional
    @Modifying
    @Query("UPDATE User u Set u.verified = FALSE where u.id = ?1")
    int unVerifyUser(int id);


}
