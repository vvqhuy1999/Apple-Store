package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Assesss;
import com.example.java5_asm_full.Entity.Categorys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Categorys, Integer>{
}
