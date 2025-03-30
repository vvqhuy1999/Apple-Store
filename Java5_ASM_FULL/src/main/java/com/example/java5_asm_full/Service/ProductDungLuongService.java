package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.PhuongThucThanhToans;
import com.example.java5_asm_full.Entity.ProductDungLuongs;

import java.util.List;

public interface ProductDungLuongService {
    List<ProductDungLuongs> findAll ();

    ProductDungLuongs findById (int theID);

    ProductDungLuongs save (ProductDungLuongs theProductDungLuongs);

    void deleteById(int theId);
}
