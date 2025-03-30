package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.DetailProductMauSacs;
import com.example.java5_asm_full.Entity.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository <Discounts, Integer>{
}
