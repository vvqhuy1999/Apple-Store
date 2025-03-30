package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.MauSacs;
import com.example.java5_asm_full.Entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository <OrderDetails, Integer>{

    @Query("SELECT od FROM OrderDetails od WHERE od.orders.madonhang = :madonhang AND od.products.masanpham = :masanpham")
    Optional<OrderDetails> findByOrdersMaDonHangAndProductMaSanPham(
            @Param("madonhang") Integer madonhang,
            @Param("masanpham") Integer masanpham
    );

//    @Query("SELECT od FROM OrderDetails od WHERE od.orders.madonhang = :madonhang")
//    List<OrderDetails> findByOrdersMadonhang(@Param("madonhang") Integer madonhang);
//
//    @Modifying
//    @Query("DELETE FROM OrderDetails od WHERE od.orders.madonhang = :madonhang")
//    void deleteByOrdersMadonhang(@Param("madonhang") Integer madonhang);
//
//    @Query("SELECT od FROM OrderDetails od WHERE od.orders.madonhang = :madonhang " +
//            "AND od.products.masanpham = :masanpham " +
//            "AND od.productDungLuongs.maproductdungluong = :maproductdungluong")
//    Optional<OrderDetails> findByOrdersMaDonHangAndProductMaSanPhamAndProductDungLuongsMaProductDungLuong(
//            @Param("madonhang") Integer madonhang,
//            @Param("masanpham") Integer masanpham,
//            @Param("maproductdungluong") Integer maproductdungluong
//    );

    List<OrderDetails> findByOrdersMadonhang(Integer madonhang);

    void deleteByOrdersMadonhang(Integer madonhang);

    @Query("SELECT od FROM OrderDetails od WHERE od.orders.madonhang = :madonhang " +
            "AND od.products.masanpham = :masanpham " +
            "AND od.productDungLuongs.maproductdungluong = :maproductdungluong")
    Optional<OrderDetails> findByOrdersMaDonHangAndProductMaSanPhamAndProductDungLuongsMaProductDungLuong(
            @Param("madonhang") Integer madonhang,
            @Param("masanpham") Integer masanpham,
            @Param("maproductdungluong") Integer maproductdungluong
    );
}
