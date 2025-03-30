package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Orders;
import com.example.java5_asm_full.Entity.Payments;

import java.util.List;

public interface PaymentService {
    List<Payments> findAll ();

    Payments findById (int theID);

    Payments save (Payments thePayments);

    void deleteById(int theId);
}
