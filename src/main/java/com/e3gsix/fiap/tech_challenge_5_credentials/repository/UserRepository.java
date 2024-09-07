package com.e3gsix.fiap.tech_challenge_5_credentials.repository;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    UserDetails findByUsername(String username);
}
