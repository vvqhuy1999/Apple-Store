package com.example.java5_asm_full.Dao;

import com.example.java5_asm_full.Entity.Orders;
import com.example.java5_asm_full.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <Payments, Integer>{
}
