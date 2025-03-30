package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.*;

import java.util.Collection;

public interface ShoppingCartService {
  Orders createCart(Integer manguoidung);
  Orders getCart(Integer manguoidung);
  OrderDetails addToCart(AddToCartRequest request);
  void removeFromCart(Integer manguoidung, Integer maChiTietDonHang);
  void updateQuantity(Integer manguoidung, Integer maChiTietDonHang, Integer soLuong);
  void clearCart(Integer manguoidung);
  Orders checkoutCart(Integer manguoidung);
}
