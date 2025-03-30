package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.OrderDetails;
import com.example.java5_asm_full.Entity.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> findAll ();

    Orders findById (int theID);

    Orders save (Orders theOrders);

    void deleteById(int theId);
}
