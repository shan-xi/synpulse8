package com.synpulse8.ebanking.security;

import com.synpulse8.ebanking.account.repo.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO define custom exception
        var account = accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user account not found"));
        // TODO define role-account schema
        var roles = new ArrayList<String>();
        roles.add("USER");
        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}