package com.synpulse8.ebanking.security;

import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EbankingUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public EbankingUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) {
        var client = clientRepository.findByUid(uid).orElseThrow(() -> new UserDataNotFoundRuntimeException("user account not found"));
        // TODO define role-account schema
        var roles = new ArrayList<String>();
        roles.add("USER");
        return User.builder()
                .username(client.getUid())
                .password(client.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}