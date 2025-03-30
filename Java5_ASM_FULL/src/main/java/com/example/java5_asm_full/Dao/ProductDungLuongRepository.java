package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.HinhAnhs;
import com.example.java5_asm_full.Entity.PhuongThucThanhToans;
import com.example.java5_asm_full.Entity.ProductDungLuongs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDungLuongRepository extends JpaRepository <ProductDungLuongs, Integer>{
    @Query("Select pd from ProductDungLuongs pd where pd.products.masanpham =:maSanPham and pd.dungluongs.madungluong = :maDungLuong")
    Optional<ProductDungLuongs> findByProductMaSanPhamAndDungLuongMaDungLuong(
            @Param("masanpham") Integer maSanPham,
            @Param("madungluong") Integer maDungLuong
    );

    @Query("SELECT pdl FROM ProductDungLuongs pdl where pdl.products.masanpham = :masanpham")
    List<ProductDungLuongs> getFirstGiaProduct(@Param("masanpham") int masanpham, Pageable pageable);

    @Query("SELECT pdl FROM ProductDungLuongs pdl where pdl.products.masanpham = :masanpham")
    List<ProductDungLuongs> getMaSanPhamforPoducDungLuong(@Param("masanpham") int masanpham );
}
