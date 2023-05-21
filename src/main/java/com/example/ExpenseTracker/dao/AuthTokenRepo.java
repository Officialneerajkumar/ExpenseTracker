package com.example.ExpenseTracker.dao;

import com.example.ExpenseTracker.model.AuthToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepo extends JpaRepository<AuthToken,Integer> {
    AuthToken findFirstByToken(String token);
    @Modifying
    @Transactional
    @Query(value = "delete from tbl_auth_token where token_id = :tokenId",nativeQuery = true)
    void deleteByTokeId(String tokenId);
}
