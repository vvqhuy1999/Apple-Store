package com.example.java5_asm_full.Controller;

import com.example.java5_asm_full.Entity.*;
import com.example.java5_asm_full.Impl.*;
import com.example.java5_asm_full.Service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductDungLuongServiceImpl productDungLuongService;

    @Autowired
    private PhuongThucThanhToanServiceImpl phuongThucThanhToanService;



    @GetMapping("/payment/checkout")
    public String showCheckout(Model model, RedirectAttributes redirectAttributes) {
        // Kiểm tra session của người dùng
        String userSession = (String) session.getAttribute("manguoidung");
        if (userSession == null || userSession.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục thanh toán!");
            return "redirect:/account/login";
        }

        Map<String, CartController.CartItem> cart = (Map<String, CartController.CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "cart/view"; // Hoặc "redirect:/cart/view"
        }

        List<CartController.CartItem> cartItems = new ArrayList<>(cart.values());
        double totalPrice = calculateTotalPrice(cartItems);
        DecimalFormat df = new DecimalFormat("#,###");

        // Lấy thông tin tài khoản
        Accounts accounts = null;
        try {
            accounts = accountService.findById(Integer.parseInt(userSession));
            if (accounts == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin tài khoản!");
                return "redirect:/account/login";
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid userSession format: " + userSession, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lấy thông tin tài khoản!");
            return "redirect:/account/login";
        }

        // Lấy danh sách phương thức thanh toán
//        List<PhuongThucThanhToans> pttt = phuongThucThanhToanService.findAll();
        List<PhuongThucThanhToans> pttt = phuongThucThanhToanService.findAllBasic();
        if (pttt == null || pttt.isEmpty()) {
            model.addAttribute("error", "Không có phương thức thanh toán nào được cấu hình!");
            return "cart/view";
        }



        model.addAttribute("pttts", pttt);
//        System.out.println("Model attribute pttts: " + pttt);
        model.addAttribute("accounts", accounts);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", df.format(totalPrice) + " VNĐ");

        logger.debug("Checkout data: cartItems={}, totalPrice={}, accounts={}, pttt={}",
                cartItems, df.format(totalPrice) + " VNĐ", accounts, pttt);

        return "payment/checkout";
    }

    /*@PostMapping("/payment/process")
    public String processPayment(@RequestParam("fullName") String fullName,
                                 @RequestParam("address") String address,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam(value = "cartItems", required = false) List<CartController.CartItem> cartItems,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // Session check
        String userSession = (String) session.getAttribute("manguoidung");
        if (userSession == null || userSession.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục thanh toán!");
            return "redirect:/account/login";
        }

        // Cart validation
        Map<String, CartController.CartItem> cart = (Map<String, CartController.CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống, không thể thanh toán!");
            return "redirect:/cart/view";
        }

        double totalPrice = calculateTotalPrice(cart.values());

        // Get account information
        Accounts account;
        try {
            int userId = Integer.parseInt(userSession);
            account = accountService.findById(userId);
            if (account == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin tài khoản!");
                return "redirect:/account/login";
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid userSession format: " + userSession, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lấy thông tin người dùng!");
            return "redirect:/account/login";
        }

        // Get payment method entity
        PhuongThucThanhToans phuongThucThanhToan;
        try {
            int paymentMethodId = Integer.parseInt(paymentMethod);
            phuongThucThanhToan = phuongThucThanhToanService.findById(paymentMethodId);
            if (phuongThucThanhToan == null) {
                redirectAttributes.addFlashAttribute("error", "Phương thức thanh toán không hợp lệ!");
                return "redirect:/payment/checkout";
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid payment method format: " + paymentMethod, e);
            redirectAttributes.addFlashAttribute("error", "Phương thức thanh toán không hợp lệ!");
            return "redirect:/payment/checkout";
        }

        try {
            // Create Order
            Orders order = new Orders();
            order.setAccounts(account);
            order.setTongtien(totalPrice);
            order.setTrangthai(2); // Đơn hàng
            order.setNgaydat(new Date());

            // Save order first to get the ID
            orderService.save(order);

            // Create Payment
            Payments payment = new Payments();
            payment.setOrders(order);
            payment.setPhuongthucthanhtoans(phuongThucThanhToan);
            payment.setTrangthaithanhtoan(1); // Assuming 1 is the initial state
            payment.setSotienthanhtoan(totalPrice);
            payment.setNgaythanhtoan(new Date());

            // Save payment
            paymentService.save(payment);

            // Create OrderDetails for each cart item
            for (CartController.CartItem item : cart.values()) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrders(order); // Liên kết với Orders

                // Lấy thông tin sản phẩm từ ProductId
                Integer productId = item.getProductId();
                Products product = productService.findById(productId);
                if (product == null) {
                    logger.warn("Product with ID={} not found, skipping this item", productId);
                    continue; // Bỏ qua nếu không tìm thấy sản phẩm
                }
                orderDetail.setProducts(product); // Liên kết với Products

                // Lấy thông tin ProductDungLuongs từ maDungLuong và productId
                Integer maDungLuong = item.getMaDungLuong();
                ProductDungLuongs productDungLuong = productDungLuongService.findByMaDungLuongAndMaSanPham(maDungLuong, productId);
                if (productDungLuong == null) {
                    logger.warn("ProductDungLuongs with maDungLuong={} and productId={} not found, skipping this item", maDungLuong, productId);
                    continue; // Bỏ qua nếu không tìm thấy ProductDungLuongs
                }
                orderDetail.setProductDungLuongs(productDungLuong); // Liên kết với ProductDungLuongs

                // Đặt số lượng
                orderDetail.setSoluong(item.getQuantity()); // Số lượng từ CartItem

                // Đặt giá từ ProductDungLuongs (giá chính xác từ database)
                orderDetail.setGia(productDungLuong.getGia()); // Lấy giá từ ProductDungLuongs thay vì CartItem

                // Save order detail
                orderDetailService.save(orderDetail);
            }

            // Clear cart after successful order creation
            session.removeAttribute("cart");

            redirectAttributes.addFlashAttribute("success", "Đơn hàng của bạn đã được đặt thành công!");
            return "redirect:/orders/confirmation/" + order.getMadonhang();

        } catch (Exception e) {
            logger.error("Error processing payment", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra trong quá trình xử lý đơn hàng!");
            return "redirect:/payment/checkout";
        }
    }*/
    @PostMapping("/payment/process")
    public String processPayment(@RequestParam("fullName") String fullName,
                                 @RequestParam("address") String address,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam(value = "cartItems", required = false) List<CartController.CartItem> cartItems,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // Session check
        String userSession = (String) session.getAttribute("manguoidung");
        if (userSession == null || userSession.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục thanh toán!");
            return "redirect:/account/login";
        }

        // Cart validation
        Map<String, CartController.CartItem> cart = (Map<String, CartController.CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống, không thể thanh toán!");
            return "redirect:/cart/view";
        }

        double totalPrice = calculateTotalPrice(cart.values());

        // Get account information (giả sử lấy từ session hoặc không cần nếu không lưu database)
        Accounts account = null; // Không cần lấy từ database nếu không lưu
        try {
            int userId = Integer.parseInt(userSession);
            account = new Accounts(); // Tạo đối tượng giả lập nếu cần
            account.setHoten(fullName);
            account.setDiachi(address);
            account.setSodienthoai(phone);
        } catch (NumberFormatException e) {
            logger.error("Invalid userSession format: " + userSession, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lấy thông tin người dùng!");
            return "redirect:/account/login";
        }

        // Get payment method (giả sử không cần lấy từ database)
        String paymentMethodName = "Thanh toán khi nhận hàng (COD)"; // Giả sử COD nếu paymentMethod = "1"
        if ("2".equals(paymentMethod)) {
            paymentMethodName = "Chuyển khoản ngân hàng";
        }
        // Hoặc bạn có thể thêm logic để ánh xạ paymentMethod với tên nếu cần

        // Lưu thông tin vào session để hiển thị bill
        session.setAttribute("billFullName", fullName);
        session.setAttribute("billAddress", address);
        session.setAttribute("billPhone", phone);
        session.setAttribute("billPaymentMethod", paymentMethodName);
        session.setAttribute("billCartItems", new ArrayList<>(cart.values())); // Lưu danh sách cartItems
        session.setAttribute("billTotalPrice", totalPrice);

        // Clear cart after successful "order" creation (dù không lưu database)
        session.removeAttribute("cart");

        redirectAttributes.addFlashAttribute("success", "Đơn hàng của bạn đã được đặt thành công!");
        return "redirect:/payment/confirmation";
    }

    // Phương thức tính totalPrice từ cart (sử dụng price * quantity)
    private double calculateTotalPrice(Iterable<CartController.CartItem> cartItems) {
        double total = 0;
        DecimalFormat df = new DecimalFormat("#,###"); // Định dạng để log hoặc xử lý
        for (CartController.CartItem item : cartItems) {
            try {
                String priceStr = item.getPrice().replace(" VNĐ", "").replace(",", ""); // Loại bỏ " VNĐ" và dấu ","
                double price = Double.parseDouble(priceStr.isEmpty() ? "0" : priceStr);
                int quantity = item.getQuantity();
                double itemTotal = price * quantity;
                total += itemTotal;
                logger.debug("Item: productName={}, price={}, quantity={}, itemTotal={}",
                        item.getProductName(), df.format(price) + " VNĐ", quantity, df.format(itemTotal) + " VNĐ");
            } catch (NumberFormatException e) {
                logger.warn("Could not parse price for item: " + item.getPrice(), e);
            }
        }
        return total;
    }

    @GetMapping("/payment/confirmation")
    public String showOrderConfirmation(Model model, HttpSession session) {
        // Lấy thông tin từ session
        String fullName = (String) session.getAttribute("billFullName");
        String address = (String) session.getAttribute("billAddress");
        String phone = (String) session.getAttribute("billPhone");
        String paymentMethod = (String) session.getAttribute("billPaymentMethod");
        List<CartController.CartItem> cartItems = (List<CartController.CartItem>) session.getAttribute("billCartItems");
        Double totalPrice = (Double) session.getAttribute("billTotalPrice");

        // Kiểm tra nếu không có dữ liệu
        if (fullName == null || cartItems == null || totalPrice == null) {
            model.addAttribute("error", "Không tìm thấy thông tin đơn hàng!");
            return "error";
        }

        // Chuẩn bị dữ liệu cho bill
        model.addAttribute("fullName", fullName);
        model.addAttribute("address", address);
        model.addAttribute("phone", phone);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        // Xóa thông tin session sau khi hiển thị (tùy chọn)
        session.removeAttribute("billFullName");
        session.removeAttribute("billAddress");
        session.removeAttribute("billPhone");
        session.removeAttribute("billPaymentMethod");
        session.removeAttribute("billCartItems");
        session.removeAttribute("billTotalPrice");

        return "payment/confirmation";
    }

}
