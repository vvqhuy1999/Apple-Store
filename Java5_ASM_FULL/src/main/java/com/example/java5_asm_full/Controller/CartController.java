package com.example.java5_asm_full.Controller;

import com.example.java5_asm_full.Entity.*;
import com.example.java5_asm_full.Impl.*;
import com.example.java5_asm_full.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private MauSacServiceImpl mauSacService;

    @Autowired
    private DungLuongServiceImpl dungLuongService;

    @Autowired
    private HinhAnhServiceImpl hinhAnhService;

    @Autowired
    private ProductDungLuongServiceImpl productDungLuongService;

    @Autowired
    private HttpSession session;

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        logger.error("Error in cart: ", e);
        model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        return "cart/view";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam("maMauSac") Integer maMauSac,
                            @RequestParam("maDungLuong") Integer maDungLuong,
                            @RequestParam("quantity") Integer quantity,
                            @RequestParam("imageUrl") String imageUrl,
                            @RequestParam("price") String price,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (productId == null || maMauSac == null || maDungLuong == null) {
                redirectAttributes.addFlashAttribute("error", "Thông tin sản phẩm không hợp lệ!");
                return "redirect:/items/index";
            }

            // Validate and set quantity
            int validQuantity = (quantity == null || quantity < 1) ? 1 : quantity;

            // Get or create cart
            Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            // Create cart key
            String cartKey = productId + "_" + maMauSac + "_" + maDungLuong;

            // Get product details
            Products product = productService.findById(productId);
            MauSacs mauSac = mauSacService.findById(maMauSac);
            DungLuongs dungLuong = dungLuongService.findById(maDungLuong);

            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/items/index";
            }

            // Create or update cart item
            CartItem item;
            if (cart.containsKey(cartKey)) {
                // Update existing item
                item = cart.get(cartKey);
                item.setQuantity(item.getQuantity() + validQuantity);
            } else {
                // Create new item
                item = new CartItem();
                item.setCartKey(cartKey);
                item.setProductId(productId);
                item.setMaMauSac(maMauSac);
                item.setMaDungLuong(maDungLuong);
                item.setQuantity(validQuantity);
                item.setImageUrl(imageUrl != null ? imageUrl : "/images/placeholder.jpg");

                // Set product details
                item.setProductName(product.getTensanpham());
                item.setColorName(mauSac != null ? mauSac.getTenmausac() : "Không xác định");
                item.setCapacity(dungLuong != null ?
                        dungLuong.getSodungluong() + " " + dungLuong.getDonvi() :
                        "Không xác định");
            }

            // Handle price
            String validPrice = price;
            if (price == null || price.trim().isEmpty() || price.equals("Liên hệ")) {
                validPrice = "0";
            }
            item.setPrice(validPrice.replace("VNĐ", "").trim());

            // Calculate total price
            try {
                double priceValue = Double.parseDouble(item.getPrice().replace(",", ""));
                DecimalFormat df = new DecimalFormat("#,###");
                item.setTotalPrice(df.format(priceValue * item.getQuantity()));
            } catch (NumberFormatException e) {
                item.setTotalPrice("0");
                logger.warn("Could not parse price: " + price, e);
            }

            // Update cart
            cart.put(cartKey, item);
            session.setAttribute("cart", cart);

            // Log success
            logger.info("Added to cart successfully: productId={}, quantity={}", productId, validQuantity);

            redirectAttributes.addFlashAttribute("message",
                    String.format("Đã thêm %d sản phẩm vào giỏ hàng!", validQuantity));

            return "redirect:/cart/view";

        } catch (Exception e) {
            logger.error("Error adding to cart: ", e);
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi thêm vào giỏ hàng: " + e.getMessage());
            return "redirect:/items/index";
        }
    }


    @GetMapping("/cart/view")
    public String viewCart(Model model) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            model.addAttribute("cart", null);
            return "cart/view";
        }

        List<CartItem> cartItems = new ArrayList<>();
        double totalPrice = 0;
        DecimalFormat df = new DecimalFormat("#,###");

        // Lấy thông tin chi tiết cho mỗi item
        for (CartItem item : cart.values()) {
            // Lấy thông tin sản phẩm
            Products product = productService.findById(item.getProductId());
            MauSacs mauSac = mauSacService.findById(item.getMaMauSac());
            DungLuongs dungLuong = dungLuongService.findById(item.getMaDungLuong());

            // Set thông tin bổ sung
            item.setProductName(product != null ? product.getTensanpham() : "Không xác định");
            item.setColorName(mauSac != null ? mauSac.getTenmausac() : "Không xác định");
            item.setCapacity(dungLuong != null ? dungLuong.getSodungluong() + " " + dungLuong.getDonvi() : "Không xác định");

            // Tính tổng giá
            double itemPrice = 0;
            try {
                itemPrice = Double.parseDouble(item.getPrice().replace(",", ""));
            } catch (Exception e) {
                itemPrice = 0;
            }
            double itemTotal = itemPrice * item.getQuantity();
            item.setTotalPrice(df.format(itemTotal));
            totalPrice += itemTotal;

            cartItems.add(item);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", df.format(totalPrice));

        return "cart/view";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("cartKey") String cartKey,
                             @RequestParam("quantity") Integer quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(cartKey)) {
            CartItem item = cart.get(cartKey);
            item.setQuantity(quantity);
            cart.put(cartKey, item);
            session.setAttribute("cart", cart);

            logger.debug("Updated cart item: cartKey={}, quantity={}", cartKey, quantity);
        }
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật số lượng sản phẩm!");
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("cartKey") String cartKey,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(cartKey);
            session.setAttribute("cart", cart);
            logger.debug("Removed from cart: cartKey={}", cartKey);
            redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
        }
        return "redirect:/cart/view";
    }

    // DTO cho CartItem
    public static class CartItem {
        private String cartKey;
        private Integer productId;
        private Integer maMauSac;
        private Integer maDungLuong;
        private String imageUrl;
        private String price;
        private int quantity;
        private String totalPrice;
        private String productName;    // Thêm trường mới
        private String colorName;      // Thêm trường mới
        private String capacity;       // Thêm trường mới

        // Constructor
        public CartItem() {}

        // Getters and Setters
        public String getCartKey() { return cartKey; }
        public void setCartKey(String cartKey) { this.cartKey = cartKey; }

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public Integer getMaMauSac() { return maMauSac; }
        public void setMaMauSac(Integer maMauSac) { this.maMauSac = maMauSac; }

        public Integer getMaDungLuong() { return maDungLuong; }
        public void setMaDungLuong(Integer maDungLuong) { this.maDungLuong = maDungLuong; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getTotalPrice() { return totalPrice; }
        public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getColorName() { return colorName; }
        public void setColorName(String colorName) { this.colorName = colorName; }

        public String getCapacity() { return capacity; }
        public void setCapacity(String capacity) { this.capacity = capacity; }
    }
}