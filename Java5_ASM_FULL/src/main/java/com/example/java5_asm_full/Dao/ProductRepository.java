package com.example.java5_asm_full.Dao;


import com.example.java5_asm_full.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Products, Integer>{
    Page<Products> findByTensanphamContaining(String keyword, Pageable pageable);
}
