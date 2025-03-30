package com.example.java5_asm_full.Dao;


import com.example.java5_asm_full.Entity.PhuongThucThanhToans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhuongThucThanhToanRepository extends JpaRepository <PhuongThucThanhToans, Integer>{
    // Thêm phương thức này để chỉ lấy thông tin cơ bản của PhuongThucThanhToans
    @Query("SELECT new PhuongThucThanhToans(p.maphuongthuc, p.tenphuongthuc, null) FROM PhuongThucThanhToans p")
    List<PhuongThucThanhToans> findAllBasicInfo();
}
