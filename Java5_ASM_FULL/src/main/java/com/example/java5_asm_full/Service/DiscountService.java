package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Categorys;
import com.example.java5_asm_full.Entity.Discounts;

import java.util.List;

public interface DiscountService {
    List<Discounts> findAll ();

    Discounts findById (int theID);

    Discounts save (Discounts theDiscounts);

    void deleteById(int theId);
}
