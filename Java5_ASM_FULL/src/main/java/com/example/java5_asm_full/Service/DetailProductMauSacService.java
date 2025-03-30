package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Categorys;
import com.example.java5_asm_full.Entity.DetailProductMauSacs;

import java.util.List;

public interface DetailProductMauSacService {
    List<DetailProductMauSacs> findAll ();

    DetailProductMauSacs findById (int theID);

    DetailProductMauSacs save (DetailProductMauSacs theDetailProductMauSacs);

    void deleteById(int theId);
}
