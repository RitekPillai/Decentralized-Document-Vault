package com.example.Decentralized.Document.Vault.repos.auth;

import com.example.Decentralized.Document.Vault.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
