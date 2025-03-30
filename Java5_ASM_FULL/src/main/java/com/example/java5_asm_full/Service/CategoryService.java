package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Assesss;
import com.example.java5_asm_full.Entity.Categorys;

import java.util.List;

public interface CategoryService {
    List<Categorys> findAll ();

    Categorys findById (int theID);

    Categorys save (Categorys theCategorys);

    void deleteById(int theId);
}
