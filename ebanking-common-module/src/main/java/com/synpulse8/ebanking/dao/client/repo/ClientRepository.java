package com.synpulse8.ebanking.dao.client.repo;

import com.synpulse8.ebanking.dao.client.dto.ClientDto;
import com.synpulse8.ebanking.dao.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUid(String uid);

    @Query("SELECT new com.synpulse8.ebanking.dao.client.dto.ClientDto(" +
            "c.id, " +
            "c.uid, " +
            "c.name) " +
            "FROM Client c " +
            "WHERE c.uid = :uid")
    Optional<ClientDto> findClientDtoByUid(@Param("uid") String uid);
}
