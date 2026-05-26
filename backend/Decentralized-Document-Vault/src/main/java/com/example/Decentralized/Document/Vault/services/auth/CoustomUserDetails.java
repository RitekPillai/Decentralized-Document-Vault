package com.example.Decentralized.Document.Vault.services.auth;

import com.example.Decentralized.Document.Vault.model.auth.User;
import com.example.Decentralized.Document.Vault.repos.auth.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CoustomUserDetails implements UserDetailsService {

    private final UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()){
            throw  new UsernameNotFoundException("User name not found Null");
        }
        return user.get();
    }
}
