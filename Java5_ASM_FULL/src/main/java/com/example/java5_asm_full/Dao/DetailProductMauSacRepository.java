package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Categorys;
import com.example.java5_asm_full.Entity.DetailProductMauSacs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailProductMauSacRepository extends JpaRepository <DetailProductMauSacs, Integer>{

    @Query("Select dpms from DetailProductMauSacs dpms where dpms.products.masanpham = :masanpham")
    List<DetailProductMauSacs> getDetailProductMauSacsByMasanpham(int masanpham);
}
