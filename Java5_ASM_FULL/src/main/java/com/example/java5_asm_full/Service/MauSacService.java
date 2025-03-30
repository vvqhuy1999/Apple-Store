package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.HinhAnhs;
import com.example.java5_asm_full.Entity.MauSacs;

import java.util.List;

public interface MauSacService {
    List<MauSacs> findAll ();

    MauSacs findById (int theID);

    MauSacs save (MauSacs theMauSacs);

    void deleteById(int theId);
}
