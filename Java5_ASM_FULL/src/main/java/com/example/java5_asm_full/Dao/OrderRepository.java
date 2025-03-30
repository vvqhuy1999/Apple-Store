package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.OrderDetails;
import com.example.java5_asm_full.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository <Orders, Integer>{

//    Optional<Orders> findByAccountsMaNguoiDungAndTrangthai(Integer maNguoiDung, Integer trangthai);
    // Sửa từ maNguoiDung thành manguoidung để khớp với tên trong entity
// Sử dụng @Query để tránh vấn đề với tên thuộc tính
    @Query("SELECT o FROM Orders o WHERE o.accounts.manguoidung = :manguoidung AND o.trangthai = :trangthai")
    Optional<Orders> findByAccountsManguoidungAndTrangthai(
            @Param("manguoidung") Integer manguoidung,
            @Param("trangthai") Integer trangthai
    );

}
