package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.DungLuongs;
import com.example.java5_asm_full.Entity.HinhAnhs;

import java.util.List;

public interface HinhAnhService {
    List<HinhAnhs> findAll ();

    HinhAnhs findById (int theID);

    HinhAnhs save (HinhAnhs theHinhAnhs);

    void deleteById(int theId);
}
