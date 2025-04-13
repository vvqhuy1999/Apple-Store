package com.example.java5_asm_full.y3;

import com.example.java5_asm_full.Controller.AccountController;
import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Service.*;
import jakarta.servlet.http.HttpSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.testng.Assert; // Đảm bảo dùng Assert của TestNG
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod; // Annotation của TestNG
import org.testng.annotations.Test; // Annotation của TestNG

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class LoginTest {
    // Đối tượng controller đang được kiểm thử
    @InjectMocks
    private AccountController accountController;

    // --- Mock cho các dependencies ---
    @Mock private CookieService cookieService;
    @Mock private ParamService paramService;
    @Mock private SessionService sessionService;
    @Mock private AccountService accountService;
    @Mock private HttpSession session;
    @Mock private Model model;
    @Mock private RedirectAttributes redirectAttributes;

    // --- Dữ liệu Test ---
    private Accounts adminAccount;
    private Accounts userAccount;
    private Accounts inputAccount;
    private Map<String, Object> existingCart;

    @BeforeMethod // Chạy trước mỗi @Test của TestNG
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Gán mock thủ công bằng ReflectionTestUtils
        ReflectionTestUtils.setField(accountController, "session", this.session);
        ReflectionTestUtils.setField(accountController, "cookieService", this.cookieService);
        ReflectionTestUtils.setField(accountController, "paramService", this.paramService);
        ReflectionTestUtils.setField(accountController, "sessionService", this.sessionService);
        ReflectionTestUtils.setField(accountController, "accountService", this.accountService);

        // Chuẩn bị dữ liệu test
        adminAccount = new Accounts();
        adminAccount.setManguoidung(1);
        adminAccount.setEmail("admin@gmail.com");
        adminAccount.setMatkhau("123456");
        adminAccount.setVaitro(1); // Vai trò Admin

        userAccount = new Accounts();
        userAccount.setManguoidung(2);
        userAccount.setEmail("ski@gmail.com");
        userAccount.setMatkhau("123");
        userAccount.setVaitro(2); // Vai trò User

        inputAccount = new Accounts();
        inputAccount.setEmail("user@example.com");
        inputAccount.setMatkhau("userpass");

        existingCart = new HashMap<>();
    }


    @Test(description = "Kiểm tra khi người dùng chưa đăng nhập, hiển thị trang login")
    public void testLogin1_NguoiDungChuaDangNhap_NenHienThiTrangLogin() {
        // Arrange
        when(session.getAttribute("manguoidung")).thenReturn(null);
        // Act
        String viewName = accountController.login1(model, redirectAttributes);
        // Assert
        Assert.assertEquals(viewName, "/account/login",
                "Khi chưa đăng nhập, controller nên trả về view '/account/login'");
        verify(model).addAttribute(eq("ac"), any(Accounts.class));
        verify(accountService, never()).findById(anyInt());
    }

    @Test(description = "Kiểm tra khi admin đã đăng nhập, chuyển hướng đến trang admin")
    public void testLogin1_AdminDaDangNhap_NenChuyenHuongDenAdminIndex() {
        // Arrange
        when(session.getAttribute("manguoidung")).thenReturn(String.valueOf(adminAccount.getManguoidung()));
        when(accountService.findById(adminAccount.getManguoidung())).thenReturn(adminAccount);
        // Act
        String viewName = accountController.login1(model, redirectAttributes);
        // Assert
        Assert.assertEquals(viewName, "redirect:/admin/index",
                "Admin đã đăng nhập, nên chuyển hướng đến 'redirect:/admin/index'");
        verify(accountService).findById(adminAccount.getManguoidung());
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test(description = "Kiểm tra khi người dùng thường đã đăng nhập, chuyển hướng đến trang người dùng")
    public void testLogin1_NguoiDungDaDangNhap_NenChuyenHuongDenItemsIndex() {
        // Arrange
        when(session.getAttribute("manguoidung")).thenReturn(String.valueOf(userAccount.getManguoidung()));
        when(accountService.findById(userAccount.getManguoidung())).thenReturn(userAccount);
        // Act
        String viewName = accountController.login1(model, redirectAttributes);
        // Assert
        Assert.assertEquals(viewName, "redirect:/items/index",
                "Người dùng đã đăng nhập, nên chuyển hướng đến 'redirect:/items/index'");
        verify(accountService).findById(userAccount.getManguoidung());
        verify(model, never()).addAttribute(anyString(), any());
    }


    // --- Tests cho POST /account/login (phương thức login2) ---

    @Test(description = "Kiểm tra đăng nhập admin thành công, không nhớ mật khẩu - CỐ TÌNH GÂY LỖI")
    public void testLogin2_AdminDangNhapThanhCong_NhoMatKhauFalse_NenChuyenHuongDenAdminIndex() {
        // Arrange
        when(paramService.getString(eq("email"), anyString())).thenReturn(adminAccount.getEmail());
        when(paramService.getString(eq("password"), anyString())).thenReturn(adminAccount.getMatkhau());
        when(paramService.getBoolean("remember", false)).thenReturn(false);
        when(accountService.findByEmailAndPass(adminAccount.getEmail(), adminAccount.getMatkhau()))
                .thenReturn(adminAccount);
        when(accountService.findById(adminAccount.getManguoidung())).thenReturn(adminAccount);
        when(session.getAttribute("cart")).thenReturn(null);

        // Act
        String viewName = accountController.login2(adminAccount, model);

        // Assert
        // <<<<<<< DÒNG NÀY ĐÃ BỊ THAY ĐỔI ĐỂ GÂY LỖI >>>>>>>>>
        Assert.assertEquals(viewName, "redirect:/SOME_WRONG_PATH", // Mong đợi đường dẫn sai
                "Admin đăng nhập thành công (không nhớ MK) nên chuyển hướng đến 'redirect:/admin/index'");
        // Các lệnh verify bên dưới sẽ không được chạy nếu Assert ở trên fail
        verify(sessionService).set("manguoidung", String.valueOf(adminAccount.getManguoidung()));
        verify(cookieService).remove("user");
        verify(cookieService, never()).add(anyString(), anyString(), anyInt());
        verify(accountService).findById(adminAccount.getManguoidung());
    }

    @Test(description = "Kiểm tra đăng nhập người dùng thành công, có nhớ mật khẩu")
    public void testLogin2_NguoiDungDangNhapThanhCong_NhoMatKhauTrue_NenChuyenHuongDenItemsIndex() {
        // Arrange
        when(paramService.getString(eq("email"), anyString())).thenReturn(userAccount.getEmail());
        when(paramService.getString(eq("password"), anyString())).thenReturn(userAccount.getMatkhau());
        when(paramService.getBoolean("remember", false)).thenReturn(true);
        when(accountService.findByEmailAndPass(userAccount.getEmail(), userAccount.getMatkhau()))
                .thenReturn(userAccount);
        when(accountService.findById(userAccount.getManguoidung())).thenReturn(userAccount);
        when(session.getAttribute("cart")).thenReturn(existingCart);
        // Act
        String viewName = accountController.login2(inputAccount, model);
        // Assert
        Assert.assertEquals(viewName, "redirect:/items/index",
                "Người dùng đăng nhập thành công (có nhớ MK) nên chuyển hướng đến 'redirect:/items/index'");
        verify(sessionService).set("manguoidung", String.valueOf(userAccount.getManguoidung()));
        verify(cookieService).add("user", String.valueOf(userAccount.getManguoidung()), 10);
        verify(cookieService, never()).remove(anyString());
        verify(accountService).findById(userAccount.getManguoidung());
        verify(session).getAttribute("cart");
    }

    @Test(description = "Kiểm tra đăng nhập thất bại do sai thông tin")
    public void testLogin2_DangNhapThatBai_ThongTinKhongHopLe_NenTraVeTrangLogin() {
        // Arrange
        String wrongEmail = "wrong@example.com";
        String wrongPass = "wrongpass";
        when(paramService.getString(eq("email"), anyString())).thenReturn(wrongEmail);
        when(paramService.getString(eq("password"), anyString())).thenReturn(wrongPass);
        when(accountService.findByEmailAndPass(wrongEmail, wrongPass)).thenReturn(null);
        Accounts wrongInputAccount = new Accounts();
        wrongInputAccount.setEmail(wrongEmail);
        wrongInputAccount.setMatkhau(wrongPass);
        // Act
        String viewName = accountController.login2(wrongInputAccount, model);
        // Assert
        Assert.assertEquals(viewName, "/account/login",
                "Đăng nhập thất bại (sai thông tin) nên trả về view '/account/login'");
        verify(sessionService, never()).set(anyString(), anyString());
        verify(cookieService, never()).add(anyString(), anyString(), anyInt());
        verify(cookieService, never()).remove(anyString());
        verify(accountService, never()).findById(anyInt());
    }
}