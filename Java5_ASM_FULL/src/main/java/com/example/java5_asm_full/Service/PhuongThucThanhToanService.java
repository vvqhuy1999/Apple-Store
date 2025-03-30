package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Payments;
import com.example.java5_asm_full.Entity.PhuongThucThanhToans;

import java.util.List;

public interface PhuongThucThanhToanService {
    List<PhuongThucThanhToans> findAll ();

    PhuongThucThanhToans findById (int theID);

    PhuongThucThanhToans save (PhuongThucThanhToans thePhuongThucThanhToans);

    void deleteById(int theId);
}
