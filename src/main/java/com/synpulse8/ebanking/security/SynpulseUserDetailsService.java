package com.synpulse8.ebanking.security;

import com.synpulse8.ebanking.account.repo.AccountRepository;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SynpulseUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public SynpulseUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) {
        // TODO define custom exception
        var account = accountRepository.findByUid(uid).orElseThrow(() -> new UserDataNotFoundRuntimeException("user account not found"));
        // TODO define role-account schema
        var roles = new ArrayList<String>();
        roles.add("USER");
        return User.builder()
                .username(account.getUid())
                .password(account.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}