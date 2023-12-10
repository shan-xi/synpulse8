package com.synpulse8.ebanking.dao.client.repo;

import com.synpulse8.ebanking.dao.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUid(String uid);
}
