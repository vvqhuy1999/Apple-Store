package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.DungLuongs;
import com.example.java5_asm_full.Entity.HinhAnhs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface HinhAnhRepository extends JpaRepository <HinhAnhs, Integer>{
    @Query("SELECT ha FROM HinhAnhs ha where ha.products.masanpham = :masanpham")
    List<HinhAnhs> getFirstImageForProduct(@Param("masanpham") int masanpham, Pageable pageable);

//    Pageable hỗ trợ phân trang

    @Query("SELECT ha FROM HinhAnhs ha where ha.products.masanpham = :masanpham")
    List<HinhAnhs> getMaSanPham(@Param("masanpham") int masanpham);
}
