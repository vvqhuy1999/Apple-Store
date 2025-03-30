package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository <Accounts, Integer>{
    @Query("SELECT a FROM Accounts a where a.email = :email and a.matkhau = :password")
    Optional<Accounts> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);


}
