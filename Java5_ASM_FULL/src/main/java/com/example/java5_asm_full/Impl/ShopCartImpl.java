package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.*;
import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Entity.Orders;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional
public class ShopCartImpl {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDungLuongRepository productDungLuongRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Orders createCart(Integer manguoidung) {
        Accounts account = accountRepository.findById(manguoidung)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Orders cart = new Orders();
        cart.setAccounts(account);
        cart.setTongtien(0.0);
        cart.setTrangthai(1); // 0 la huy | 1 la gio hang | 2 don hang
        cart.setNgaydat(new Date());
        cart.setOrderdetails(new ArrayList<>()); // Khởi tạo danh sách orderdetails
        return orderRepository.save(cart);
    }

    public Orders getCart(Integer manguoidung) {
        return orderRepository.findByAccountsManguoidungAndTrangthai(manguoidung, 0)
                .orElseGet(() -> createCart(manguoidung));
    }
}
