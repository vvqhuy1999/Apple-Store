package com.example.java5_asm_full.Controller;

import org.springframework.ui.Model;
import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Entity.HinhAnhs;
import com.example.java5_asm_full.Entity.ProductDungLuongs;
import com.example.java5_asm_full.Entity.Products;
import com.example.java5_asm_full.Impl.HinhAnhServiceImpl;
import com.example.java5_asm_full.Impl.ProductDungLuongServiceImpl;
import com.example.java5_asm_full.Service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    CookieService cookieService;

    @Autowired
    ParamService paramService;

    @Autowired
    SessionService sessionService;

    @Autowired
    AccountService accountService;
    @Autowired
    private HttpSession session;
    @Autowired
    ProductService productService;
    @Autowired
    HinhAnhServiceImpl hinhAnhService;
    @Autowired
    ProductDungLuongServiceImpl productDungLuongService;

    @GetMapping("/account/login")
    public String login1(Model model, RedirectAttributes redirectAttributes){
//        String userSession = sessionService.get("manguoidung");
        String userSession = (String) session.getAttribute("manguoidung");
        if (userSession != null) {
            Accounts accounts = null;
            accounts = accountService.findById(Integer.parseInt(userSession));
            System.out.println(accounts);
            if (accounts.getVaitro() == 1) return "redirect:/admin/index";
            else return "redirect:/items/index";
        }
        model.addAttribute("ac", new Accounts()); // Khởi tạo object cho form nếu chưa đăng nhập
        return "/account/login";
    }
    @PostMapping("/account/login")
    public String login2(@ModelAttribute("ac") Accounts account, Model model) {
        String un = paramService.getString("email", account.getEmail());
        String pw = paramService.getString("password", account.getMatkhau());
        boolean rm = paramService.getBoolean("remember", false);

        Accounts loggedInAccount = accountService.findByEmailAndPass(un, pw);
        Accounts accounts = null;
        if (loggedInAccount != null) {
            String id =  String.valueOf(loggedInAccount.getManguoidung());

            sessionService.set("manguoidung",id);
            // Kiểm tra và giữ lại cart nếu đã tồn tại
            Map<String, CartController.CartItem> cart = (Map<String, CartController.CartItem>) session.getAttribute("cart");
            if (cart != null) {
                logger.debug("Cart preserved during login: {}", cart);
            }

            if (rm) {
                cookieService.add("user", id, 10);
            } else {
                cookieService.remove("user");
            }
            accounts = accountService.findById(Integer.parseInt(id));
            if(accounts.getVaitro() == 1) return "redirect:/admin/index";
            else return "redirect:/items/index"; // Chuyển hướng đến trang danh sách item
        } else {
            // Nếu thông tin đăng nhập không hợp lệ
            return "/account/login"; // Chuyển hướng trở lại trang đăng nhập
        }
    }

    @GetMapping("/admin/index")
    public String DSSanPhamAdmin(org.springframework.ui.Model model,
                                 @PageableDefault(page = 0, size = 6, sort = "masanpham", direction = Sort.Direction.ASC) Pageable pageable)
    {
        String userSession = (String) session.getAttribute("manguoidung");
        if (userSession != null) {

            // Lấy danh sách sản phẩm phân trang từ productService
            Page<Products> productPage = productService.findAllPage(pageable);

            DecimalFormat df = new DecimalFormat("#,###");
            List<Products> resultProduct = productService.findAll() != null ? productService.findAll() : Collections.emptyList();

            // Chuyển đổi Page<Products> thành Page<ProductDTO>
            Page<ItemsController.ProductDTO> productDTOPage = productPage.map(p -> {
                List<HinhAnhs> hinhAnhs = hinhAnhService.findOneForProduct(p.getMasanpham());
                List<String> hinhAnhLinks = hinhAnhs != null ?
                        hinhAnhs.stream().map(HinhAnhs::getLinkhinhanh).collect(Collectors.toList()) :
                        new ArrayList<>();

                List<ProductDungLuongs> dungLuongs = productDungLuongService.findOneForGiaProduct(p.getMasanpham());
                List<Double> giaList = dungLuongs != null ?
                        dungLuongs.stream().map(ProductDungLuongs::getGia).collect(Collectors.toList()) :
                        new ArrayList<>();

                Collections.reverse(giaList);
                String giaFormatted = giaList.isEmpty() ? "N/A" : df.format(giaList.get(0));
                return new ItemsController.ProductDTO(p.getMasanpham(), p.getTensanpham(), hinhAnhLinks, giaFormatted);
            });

//        productDTOs.forEach(dto ->
//                System.out.println("SanPham: " + dto.getTensanpham() + ", HinhAnhLinks: " + dto.getHinhAnhLinks() + ", Gia: " + dto.getGia()));

            model.addAttribute("products", productDTOPage.getContent());
            model.addAttribute("page", productDTOPage); // Thêm đối tượng Page để sử dụng trong view
            model.addAttribute("keyword", null); // Đặt keyword là null cho danh sách thông thường
            return "account/admin";
        }
        return "redirect:/account/login";

    }

    @GetMapping("/admin/search")
    public String searchAdminProducts(@RequestParam("keyword") String keyword,
                                      Model model,
                                      @PageableDefault(page = 0, size = 10, sort = "masanpham", direction = Sort.Direction.ASC) Pageable pageable) {
        DecimalFormat df = new DecimalFormat("#,###");

        Page<Products> productPage = productService.findByTensanphamContaining(keyword, pageable);

        Page<ItemsController.ProductDTO> productDTOPage = productPage.map(p -> {
            List<HinhAnhs> hinhAnhs = hinhAnhService.findOneForProduct(p.getMasanpham());
            List<String> hinhAnhLinks = hinhAnhs != null ?
                    hinhAnhs.stream().map(HinhAnhs::getLinkhinhanh).collect(Collectors.toList()) :
                    new ArrayList<>();

            List<ProductDungLuongs> dungLuongs = productDungLuongService.findOneForGiaProduct(p.getMasanpham());
            List<Double> giaList = dungLuongs != null ?
                    dungLuongs.stream().map(ProductDungLuongs::getGia).collect(Collectors.toList()) :
                    new ArrayList<>();

            Collections.reverse(giaList);
            String giaFormatted = giaList.isEmpty() ? "N/A" : df.format(giaList.get(0));
            return new ItemsController.ProductDTO(p.getMasanpham(), p.getTensanpham(), hinhAnhLinks, giaFormatted);
        });

        model.addAttribute("products", productDTOPage.getContent());
        model.addAttribute("page", productDTOPage);
        model.addAttribute("keyword", keyword);
        return "redirect:account/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        Products product = productService.findById(id);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại!");
            return "admin/index";
        }
        model.addAttribute("product", product);
        return "account/edit";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa sản phẩm thất bại: " + e.getMessage());
        }
        return "redirect:account/admin";
    }

    @GetMapping("/account/logout")
    public String logout() {
        session.removeAttribute("manguoidung");
        return "redirect:/account/login";
    }

    @PostMapping("/account/signup")
    public String signup(Model model, RedirectAttributes redirectAttributes) {
        // Lấy dữ liệu từ form
        String fullname = paramService.getString("fullName", "");
        String email = paramService.getString("email", "");
        String password = paramService.getString("password", "");
        String phone = paramService.getString("phone", "");
        String address = paramService.getString("address", "");

        // Kiểm tra dữ liệu
        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("signupMessage", "Vui lòng điền đầy đủ thông tin");
            return "redirect:/account/login";
        }

        // Tạo đối tượng tài khoản mới
        Accounts account = new Accounts();
        account.setHoten(fullname);
        account.setEmail(email);
        account.setMatkhau(password);
        account.setSodienthoai(phone);
        account.setDiachi(address);

        try {
            // Lưu tài khoản
            accountService.save(account);
            redirectAttributes.addFlashAttribute("signupMessage", "Đăng ký thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("signupMessage", "Đăng ký thất bại: " + e.getMessage());
        }

        return "redirect:/account/login";
    }


}
