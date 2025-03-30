package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Ships;
import com.example.java5_asm_full.Entity.UseDiscounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseDiscountRepository extends JpaRepository <UseDiscounts, Integer>{
}
