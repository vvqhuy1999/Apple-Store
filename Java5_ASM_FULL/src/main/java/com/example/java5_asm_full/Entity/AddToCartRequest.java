package com.example.java5_asm_full.Entity;

import lombok.Data;

// Request DTO
@Data
public class AddToCartRequest {
    private Integer maNguoiDung;
    private Integer maSanPham;
    private Integer maDungLuong;
    private Integer soLuong;
}
