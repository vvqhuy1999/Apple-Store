package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.*;
import com.example.java5_asm_full.Entity.*;
import com.example.java5_asm_full.Service.ShoppingCartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class ShoppingCartServiceImpl  implements ShoppingCartService {

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


    @Override
    public Orders createCart(Integer manguoidung) {
        Accounts account = accountRepository.findById(manguoidung)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Orders cart = new Orders();
        cart.setAccounts(account);
        cart.setTongtien(0.0);
        cart.setTrangthai(1); // 0 = Giỏ hàng, 1 = Đơn hàng chờ xác nhận
        cart.setNgaydat(new Date());
        cart.setOrderdetails(new ArrayList<>()); // Khởi tạo danh sách orderdetails
        return orderRepository.save(cart);
    }

    @Override
    public Orders getCart(Integer manguoidung) {
//        return orderRepository.findByAccountsMaNguoiDungAndTrangthai(manguoidung, 0)
//                .orElseGet(() -> createCart(manguoidung));
        return orderRepository.findByAccountsManguoidungAndTrangthai(manguoidung, 1)
                .orElseGet(() -> createCart(manguoidung));
    }

    @Override
    public OrderDetails addToCart(AddToCartRequest request) {
        if (request.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        Orders cart = getCart(request.getMaNguoiDung());

        ProductDungLuongs productDungLuong = productDungLuongRepository
                .findByProductMaSanPhamAndDungLuongMaDungLuong(
                        request.getMaSanPham(),
                        request.getMaDungLuong()
                )
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (productDungLuong.getSoluongkho() < request.getSoLuong()) {
            throw new IllegalStateException("Số lượng trong kho không đủ");
        }

        Optional<OrderDetails> existingItem = orderDetailRepository
                .findByOrdersMaDonHangAndProductMaSanPhamAndProductDungLuongsMaProductDungLuong(
                        cart.getMadonhang(),
                        request.getMaSanPham(),
                        productDungLuong.getMaproductdungluong()
                );

        OrderDetails cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            int newQuantity = cartItem.getSoluong() + request.getSoLuong();
            if (productDungLuong.getSoluongkho() < newQuantity) {
                throw new IllegalStateException("Số lượng trong kho không đủ");
            }
            cartItem.setSoluong(newQuantity);
            cartItem.setGia(productDungLuong.getGia());
        } else {
            cartItem = new OrderDetails();
            cartItem.setOrders(cart);
            cartItem.setProducts(productRepository.getReferenceById(request.getMaSanPham()));
            cartItem.setProductDungLuongs(productDungLuong); // Thêm reference đến ProductDungLuongs
            cartItem.setSoluong(request.getSoLuong());
            cartItem.setGia(productDungLuong.getGia());
        }

        cartItem = orderDetailRepository.save(cartItem);
        updateCartTotal(cart);

        return cartItem;
    }

    @Override
    public void removeFromCart(Integer manguoidung, Integer maChiTietDonHang) {
        Orders cart = getCart(manguoidung);

        OrderDetails item = orderDetailRepository.findById(maChiTietDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cart.getMadonhang().equals(item.getOrders().getMadonhang())) {
            throw new RuntimeException("Sản phẩm không thuộc giỏ hàng của bạn");
        }

        orderDetailRepository.delete(item);
        updateCartTotal(cart);
    }

    @Override
    public void updateQuantity(Integer manguoidung, Integer maChiTietDonHang, Integer soLuong) {
        if (soLuong <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        Orders cart = getCart(manguoidung);

        OrderDetails item = orderDetailRepository.findById(maChiTietDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cart.getMadonhang().equals(item.getOrders().getMadonhang())) {
            throw new RuntimeException("Sản phẩm không thuộc giỏ hàng của bạn");
        }

        // Kiểm tra tồn kho
        ProductDungLuongs productDungLuong = item.getProductDungLuongs();
        if (productDungLuong == null) {
            throw new RuntimeException("Không tìm thấy thông tin sản phẩm");
        }

        if (productDungLuong.getSoluongkho() < soLuong) {
            throw new RuntimeException("Số lượng trong kho không đủ");
        }

        item.setSoluong(soLuong);
        orderDetailRepository.save(item);
        updateCartTotal(cart);
    }

    @Override
    public void clearCart(Integer manguoidung) {
        Orders cart = getCart(manguoidung);
        orderDetailRepository.deleteByOrdersMadonhang(cart.getMadonhang());
        cart.setTongtien(0.0);
        orderRepository.save(cart);
    }

    @Override
    public Orders checkoutCart(Integer manguoidung) {
        Orders cart = getCart(manguoidung);

        // Kiểm tra giỏ hàng có trống không
        if (cart.getOrderdetails().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // Kiểm tra tồn kho và cập nhật số lượng tồn cho tất cả sản phẩm
        for (OrderDetails item : cart.getOrderdetails()) {
            ProductDungLuongs productDungLuong = item.getProductDungLuongs();
            if (productDungLuong == null) {
                throw new RuntimeException(
                        "Không tìm thấy thông tin dung lượng cho sản phẩm " +
                                item.getProducts().getTensanpham()
                );
            }

            if (productDungLuong.getSoluongkho() < item.getSoluong()) {
                throw new RuntimeException(
                        "Sản phẩm " + item.getProducts().getTensanpham() +
                                " (" + productDungLuong.getDungluongs().getSodungluong() +
                                productDungLuong.getDungluongs().getDonvi() +
                                ") không đủ số lượng trong kho"
                );
            }

            // Cập nhật số lượng tồn kho
            productDungLuong.setSoluongkho(
                    productDungLuong.getSoluongkho() - item.getSoluong()
            );
            productDungLuongRepository.save(productDungLuong);
        }

        // Chuyển giỏ hàng thành đơn hàng
        cart.setTrangthai(1); // Chuyển sang trạng thái đơn hàng chờ xác nhận
        cart.setNgaydat(new Date());
        orderRepository.save(cart);

        // Tạo giỏ hàng mới
        createCart(manguoidung);

        return cart;
    }

    private void updateCartTotal(Orders cart) {
        double total = orderDetailRepository.findByOrdersMadonhang(cart.getMadonhang())
                .stream()
                .mapToDouble(item -> item.getGia() * item.getSoluong())
                .sum();

        cart.setTongtien(total);
        orderRepository.save(cart);
    }
}
