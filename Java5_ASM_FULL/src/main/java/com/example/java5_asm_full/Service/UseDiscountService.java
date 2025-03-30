package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Ships;
import com.example.java5_asm_full.Entity.UseDiscounts;

import java.util.List;

public interface UseDiscountService {
    List<UseDiscounts> findAll ();

    UseDiscounts findById (int theID);

    UseDiscounts save (UseDiscounts theUseDiscounts);

    void deleteById(int theId);
}
