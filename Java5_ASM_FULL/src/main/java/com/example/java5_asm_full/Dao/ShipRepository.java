package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Products;
import com.example.java5_asm_full.Entity.Ships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShipRepository extends JpaRepository <Ships, Integer>{

}
