package com.example.links.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.links.entity.CustomUser;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    @Query(value = "select u from CustomUser u where u.email = :email")
    Optional<CustomUser> findByEmail(String email);

    @Query(value = "select u.id from CustomUser u where u.email = :email")
    Optional<Long> findUserIdByEmail(String email);
}
