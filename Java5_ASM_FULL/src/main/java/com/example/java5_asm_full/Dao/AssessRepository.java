package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Entity.Assesss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessRepository extends JpaRepository <Assesss, Integer>{
}
