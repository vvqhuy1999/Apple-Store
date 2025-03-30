package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.MauSacs;
import com.example.java5_asm_full.Entity.OrderDetails;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetails> findAll ();

    OrderDetails findById (int theID);

    OrderDetails save (OrderDetails theOrderDetails);

    void deleteById(int theId);
}
