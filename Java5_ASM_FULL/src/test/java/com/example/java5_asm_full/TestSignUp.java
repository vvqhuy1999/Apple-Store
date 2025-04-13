package com.example.java5_asm_full;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestSignUp {
    private WebDriver driver;
    private String baseUrl = "http://localhost:8080"; // URL
    private WebDriverWait wait;

    private static boolean resultsSaved = false;

    // Khai báo map để lưu trữ các phần tử form
    private Map<String, WebElement> formElements;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // Explicit wait với thời gian dài hơn

        // Khởi tạo map để lưu trữ các phần tử form
        formElements = new HashMap<>();

        // Kiểm tra xem server có hoạt động không trước khi bắt đầu test
        try {
            driver.get(baseUrl);
            System.out.println("Kết nối đến server thành công: " + driver.getTitle());
            TestResultManager.put("Setup", 0.0, "Kết nối đến server",
                    "Kết nối thành công", "Pass");
        } catch (Exception e) {
            System.err.println("Không thể kết nối đến server. Kiểm tra lại URL và tình trạng server.");
            TestResultManager.put("Setup", 0.0, "Kết nối đến server",
                    "Kết nối thành công", "Fail");
            Assert.fail("Lỗi kết nối: " + e.getMessage());
        }
    }

    // Phương thức helper để đợi trang tải hoàn tất
    private void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    // Phương thức helper để log thông tin trang hiện tại (hỗ trợ debug)
    private void logPageInfo(String testName) {
        System.out.println("--- " + testName + " ---");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
    }

    // Phương thức helper để kiểm tra thuộc tính
    private boolean isAttributePresent(WebElement element, String attribute) {
        try {
            String value = element.getAttribute(attribute);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Phương thức để mở tab đăng ký
    private void openSignupTab() {
        // Mở trang đăng nhập/đăng ký
        driver.get(baseUrl + "/account/login");
        waitForPageLoad();

        // Nhấp vào tab Đăng Ký
        WebElement signupTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("signup-tab")));
        signupTab.click();

        // Đợi tab đăng ký hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup")));

        // Tìm và lưu trữ các phần tử form
        findSignupFormElements();
    }

    // Phương thức để tìm và lưu trữ các phần tử form đăng ký
    private void findSignupFormElements() {
        formElements.clear(); // Xóa các phần tử đã lưu trữ trước đó

        formElements.put("fullnameField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupFullName"))));
        formElements.put("emailField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupEmail"))));
        formElements.put("passwordField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupPassword"))));
        formElements.put("confirmPasswordField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupConfirmPassword"))));
        formElements.put("phoneField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupPhone"))));
        formElements.put("addressField", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupAddress"))));
        formElements.put("signupButton", wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Đăng Ký')]"))));
    }

    // Phương thức để lấy phần tử form theo tên
    private WebElement getFormElement(String elementName) {
        if (!formElements.containsKey(elementName)) {
            throw new IllegalArgumentException("Phần tử '" + elementName + "' không tồn tại trong form");
        }
        return formElements.get(elementName);
    }

    // Phương thức để nhập văn bản với điều kiện chờ
    private void enterTextWithWait(WebElement element, String text) {
        if (text != null) {
            wait.until(ExpectedConditions.visibilityOf(element)); // Đợi cho phần tử khả dụng
            element.clear(); // Xóa trường nếu cần thiết
            element.sendKeys(text); // Nhập văn bản
        }
    }

    // Phương thức để điền thông tin vào form đăng ký
    private void fillSignupFormWithWait(String fullname, String email, String password,
                                        String confirmPassword, String phone, String address) {
        enterTextWithWait(getFormElement("fullnameField"), fullname);
        enterTextWithWait(getFormElement("emailField"), email);
        enterTextWithWait(getFormElement("passwordField"), password);
        enterTextWithWait(getFormElement("confirmPasswordField"), confirmPassword);
        enterTextWithWait(getFormElement("phoneField"), phone);
        enterTextWithWait(getFormElement("addressField"), address);
    }

    // Phương thức để nhấn nút đăng ký
    private void clickSignupButton() {
        getFormElement("signupButton").click();
    }

    // Phương thức để kiểm tra thông báo lỗi cho các trường
    private void checkErrorMessageForField(String expectedMessage, String field, String testCase, double step) {
        waitForPageLoad();
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h5[@class='text-danger text-center mt-3']")));

            boolean hasErrorMessage = errorMessage.isDisplayed() && errorMessage.getText().contains(expectedMessage);

            System.out.println("Error message: " + errorMessage.getText());

            if (hasErrorMessage) {
                TestResultManager.put(testCase, step, "Kiểm tra thông báo lỗi từ server",
                        "Hiển thị thông báo lỗi cho trường " + field, "Pass");
                Assert.assertTrue(hasErrorMessage, "Không hiển thị thông báo lỗi cho trường " + field);
            } else {
                TestResultManager.put(testCase, step, "Kiểm tra thông báo lỗi từ server",
                        "Hiển thị thông báo lỗi cho trường " + field, "Fail");
                Assert.fail("Không hiển thị thông báo lỗi cho trường " + field);
            }
        } catch (Exception e) {
            // Kiểm tra nếu thông báo lỗi xuất hiện trong page source
            String pageSource = driver.getPageSource();
            boolean containsErrorText = pageSource.contains(expectedMessage);

            if (containsErrorText) {
                TestResultManager.put(testCase, step, "Kiểm tra thông báo lỗi trong page source",
                        "Hiển thị thông báo lỗi cho trường " + field, "Pass");
                Assert.assertTrue(containsErrorText, "Không tìm thấy thông báo lỗi cho trường " + field);
            } else {
                TestResultManager.put(testCase, step, "Kiểm tra thông báo lỗi trong page source",
                        "Hiển thị thông báo lỗi cho trường " + field, "Fail");
                Assert.fail("Không tìm thấy thông báo lỗi cho trường " + field);
            }
        }
    }

    @AfterMethod
    public void afterMethod1() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 1, description = "TC01 - Đăng ký thành công")
    public void testSignupSuccess() {
        try {
            // Mở tab đăng ký
            openSignupTab();
            logPageInfo("TC01 - Đăng ký thành công");
            TestResultManager.put("TC01", 1.0, "Mở tab đăng ký",
                    "Tab đăng ký hiển thị", "Pass");

            // Tạo email ngẫu nhiên để tránh trùng lặp
            String randomEmail = "user" + System.currentTimeMillis() + "@example.com";

            // Điền thông tin hợp lệ vào form đăng ký
            fillSignupFormWithWait("Nguyễn Văn A", randomEmail, "Password123", "Password123",
                    "0987654321", "123 Đường ABC");
            TestResultManager.put("TC01", 2.0, "Nhập dữ liệu hợp lệ",
                    "Các trường dữ liệu đã được nhập", "Pass");

            // Nhấn nút đăng ký
            clickSignupButton();
            waitForPageLoad();

            // Kiểm tra chuyển hướng sau khi đăng ký thành công
            try {
                // Đợi cho đến khi chuyển hướng đến trang đăng nhập hoặc trang chủ
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.urlContains("/account/login"),
                        ExpectedConditions.urlContains("/items/index")
                ));

                String currentUrl = driver.getCurrentUrl();
                boolean isRedirected = currentUrl.contains("/account/login") || currentUrl.contains("/items/index");

                if (isRedirected) {
                    TestResultManager.put("TC01", 3.0, "Kiểm tra chuyển hướng sau đăng ký",
                            "Chuyển hướng thành công", "Pass");
                    Assert.assertTrue(isRedirected, "Không chuyển hướng sau khi đăng ký thành công");

                    // Kiểm tra thêm thông báo thành công nếu có
                    if (driver.getPageSource().contains("Đăng ký thành công") ||
                            driver.getPageSource().contains("đăng ký thành công") ||
                            driver.getPageSource().contains("registration successful")) {
                        TestResultManager.put("TC01", 4.0, "Kiểm tra thông báo thành công",
                                "Hiển thị thông báo đăng ký thành công", "Pass");
                    }
                } else {
                    TestResultManager.put("TC01", 3.0, "Kiểm tra chuyển hướng sau đăng ký",
                            "Chuyển hướng thành công", "Fail");
                    Assert.fail("Không chuyển hướng sau khi đăng ký thành công");
                }
            } catch (Exception e) {
                TestResultManager.put("TC01", 3.0, "Kiểm tra chuyển hướng sau đăng ký",
                        "Chuyển hướng thành công", "Fail");
                Assert.fail("Lỗi khi kiểm tra chuyển hướng: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("TC01 Error: " + e.getMessage());
            TestResultManager.put("TC01", 5.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng ký thành công thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod2() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }
    @Test(priority = 2, description = "TC02 - Đăng ký với trường Họ tên bỏ trống")
    public void testSignupWithEmptyFullname() {
        try {
            // Mở tab đăng ký
            openSignupTab();
            logPageInfo("TC02 - Đăng ký với trường Họ tên bỏ trống");
            TestResultManager.put("TC02", 1.0, "Mở tab đăng ký",
                    "Tab đăng ký hiển thị", "Pass");

            // Điền thông tin vào form đăng ký, để trống trường Họ tên
            fillSignupFormWithWait(null, "test@example.com", "Password123", "Password123",
                    "0987654321", "123 Đường ABC");
            TestResultManager.put("TC02", 2.0, "Nhập dữ liệu với Họ tên bỏ trống",
                    "Các trường dữ liệu đã được nhập", "Pass");

            // Nhấn nút đăng ký
            clickSignupButton();

            // Kiểm tra HTML5 validation
            WebElement fullnameField = getFormElement("fullnameField");
            boolean isInvalid = driver.findElements(By.cssSelector("#signupFullName:invalid")).size() > 0;
            boolean hasRequiredAttr = isAttributePresent(fullnameField, "required");

            if (isInvalid || hasRequiredAttr) {
                TestResultManager.put("TC02", 3.0, "Kiểm tra HTML5 validation",
                        "HTML5 validation hiển thị cho trường Họ tên", "Pass");
                Assert.assertTrue(isInvalid || hasRequiredAttr, "Không hiển thị lỗi HTML5 validation khi bỏ trống Họ tên");
            } else {
                // Kiểm tra thông báo lỗi
                checkErrorMessageForField("Họ tên không được để trống", "Họ tên", "TC02", 3.0);
            }
        } catch (Exception e) {
            System.err.println("TC02 Error: " + e.getMessage());
            TestResultManager.put("TC02", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng ký với trường Họ tên bỏ trống thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod3() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 3, description = "TC03 - Đăng ký với trường Email bỏ trống")
    public void testSignupWithEmptyEmail() {
        try {
            // Mở tab đăng ký
            openSignupTab();
            logPageInfo("TC03 - Đăng ký với trường Email bỏ trống");
            TestResultManager.put("TC03", 1.0, "Mở tab đăng ký",
                    "Tab đăng ký hiển thị", "Pass");

            // Điền thông tin vào form đăng ký, để trống trường Email
            fillSignupFormWithWait("Nguyễn Văn B", null, "Password123", "Password123",
                    "0987654321", "123 Đường ABC");
            TestResultManager.put("TC03", 2.0, "Nhập dữ liệu với Email bỏ trống",
                    "Các trường dữ liệu đã được nhập", "Pass");

            // Nhấn nút đăng ký
            clickSignupButton();

            // Kiểm tra HTML5 validation
            WebElement emailField = getFormElement("emailField");
            boolean isInvalid = driver.findElements(By.cssSelector("#signupEmail:invalid")).size() > 0;
            boolean hasRequiredAttr = isAttributePresent(emailField, "required");

            if (isInvalid || hasRequiredAttr) {
                TestResultManager.put("TC03", 3.0, "Kiểm tra HTML5 validation",
                        "HTML5 validation hiển thị cho trường Email", "Pass");
                Assert.assertTrue(isInvalid || hasRequiredAttr, "Không hiển thị lỗi HTML5 validation khi bỏ trống Email");
            } else {
                // Kiểm tra thông báo lỗi
                checkErrorMessageForField("Email không được để trống", "Email", "TC03", 3.0);
            }
        } catch (Exception e) {
            System.err.println("TC03 Error: " + e.getMessage());
            TestResultManager.put("TC03", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng ký với trường Email bỏ trống thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod4() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 4, description = "TC04 - Đăng ký với Email không đúng định dạng")
    public void testSignupWithInvalidEmail() {
        try {
            // Mở tab đăng ký
            openSignupTab();
            logPageInfo("TC04 - Đăng ký với Email không đúng định dạng");
            TestResultManager.put("TC04", 1.0, "Mở tab đăng ký",
                    "Tab đăng ký hiển thị", "Pass");

            // Điền thông tin vào form đăng ký với email không hợp lệ
            fillSignupFormWithWait("Nguyễn Văn C", "invalid-email", "Password123", "Password123",
                    "0987654321", "123 Đường ABC");
            TestResultManager.put("TC04", 2.0, "Nhập dữ liệu với Email không đúng định dạng",
                    "Các trường dữ liệu đã được nhập", "Pass");

            // Nhấn nút đăng ký
            clickSignupButton();

            // Kiểm tra HTML5 validation
            WebElement emailField = getFormElement("emailField");
            boolean isInvalid = driver.findElements(By.cssSelector("#signupEmail:invalid")).size() > 0;

            if (isInvalid) {
                TestResultManager.put("TC04", 3.0, "Kiểm tra HTML5 validation",
                        "HTML5 validation hiển thị cho Email không đúng định dạng", "Pass");
                Assert.assertTrue(isInvalid, "Không hiển thị lỗi HTML5 validation khi nhập Email không đúng định dạng");
            } else {
                // Kiểm tra thông báo lỗi
                checkErrorMessageForField("Email không đúng định dạng", "Email", "TC04", 3.0);
            }
        } catch (Exception e) {
            System.err.println("TC04 Error: " + e.getMessage());
            TestResultManager.put("TC04", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng ký với Email không đúng định dạng thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod5() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 5, description = "TC05 - Đăng ký với Email đã tồn tại")
    public void testSignupWithExistingEmail() {
        try {
            // Mở tab đăng ký
            openSignupTab();
            logPageInfo("TC05 - Đăng ký với Email đã tồn tại");
            TestResultManager.put("TC05", 1.0, "Mở tab đăng ký",
                    "Tab đăng ký hiển thị", "Pass");

            // Điền thông tin vào form đăng ký với email đã tồn tại
            fillSignupFormWithWait("Nguyễn Văn D", "user1@gmail.com", "Password123", "Password123",
                    "0987654321", "123 Đường ABC");
            TestResultManager.put("TC05", 2.0, "Nhập dữ liệu với Email đã tồn tại",
                    "Các trường dữ liệu đã được nhập", "Pass");

            // Nhấn nút đăng ký
            clickSignupButton();

            // Kiểm tra thông báo lỗi
            checkErrorMessageForField("Email đã được sử dụng", "Email", "TC05", 3.0);
        } catch (Exception e) {
            System.err.println("TC05 Error: " + e.getMessage());
            TestResultManager.put("TC05", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng ký với Email đã tồn tại thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod6() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 6, description = "TC06 - Đăng ký với Mật khẩu trống")
    public void testSignupWithEmptyPassword() {
        try {
            openSignupTab();
            logPageInfo("TC06 - Mật khẩu trống");
            TestResultManager.put("TC06", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait("Nguyễn Văn E", "nguyenvane@example.com",
                    null, null, "0987654321", "123 Đường ABC");
            TestResultManager.put("TC06", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            // Kiểm tra cả hai trường mật khẩu
            checkDoubleFieldError("Mật khẩu không được để trống",
                    "passwordField", "confirmPasswordField", "TC06", 3.0);
        } catch (Exception e) {
            handleTestError("TC06", e);
        }
    }

    @AfterMethod
    public void afterMethod7() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 7, description = "TC07 - Mật khẩu không khớp")
    public void testSignupWithPasswordMismatch() {
        try {
            openSignupTab();
            logPageInfo("TC07 - Mật khẩu không khớp");
            TestResultManager.put("TC07", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait("Nguyễn Văn F", "nguyenvanf@example.com",
                    "Password123", "Password456", "0987654321", "123 Đường ABC");
            TestResultManager.put("TC07", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            checkErrorMessageForField("Mật khẩu và xác nhận mật khẩu không khớp",
                    "confirmPasswordField", "TC07", 3.0);
        } catch (Exception e) {
            handleTestError("TC07", e);
        }
    }

    @AfterMethod
    public void afterMethod8() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 8, description = "TC08 - Số điện thoại trống")
    public void testSignupWithEmptyPhone() {
        try {
            openSignupTab();
            logPageInfo("TC08 - SĐT trống");
            TestResultManager.put("TC08", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait("Nguyễn Văn G", "nguyenvang@example.com",
                    "Password123", "Password123", null, "123 Đường ABC");
            TestResultManager.put("TC08", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            checkErrorMessageForField("Số điện thoại không được để trống",
                    "phoneField", "TC08", 3.0);
        } catch (Exception e) {
            handleTestError("TC08", e);
        }
    }

    @AfterMethod
    public void afterMethod9() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }
    @Test(priority = 9, description = "TC09 - Số điện thoại đã tồn tại")
    public void testSignupWithExistingPhone() {
        try {
            openSignupTab();
            logPageInfo("TC09 - SĐT tồn tại");
            TestResultManager.put("TC09", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait("Nguyễn Văn H", "nguyenvanh@example.com",
                    "Password123", "Password123", "0123456789", "123 Đường ABC");
            TestResultManager.put("TC09", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            checkErrorMessageForField("Số điện thoại đã được sử dụng",
                    "phoneField", "TC09", 3.0);
        } catch (Exception e) {
            handleTestError("TC09", e);
        }
    }

    @AfterMethod
    public void afterMethod10() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 10, description = "TC11 - Đăng ký không nhập Địa chỉ")
    public void testSignupWithEmptyAddress() {
        try {
            openSignupTab();
            logPageInfo("TC11 - Địa chỉ trống");
            TestResultManager.put("TC11", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait("Nguyễn Văn J", "nguyenvanj@example.com",
                    "Password123", "Password123", "0987654322", null);
            TestResultManager.put("TC11", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            // Kiểm tra đăng ký thành công
            wait.until(ExpectedConditions.urlContains("/login"));
            TestResultManager.put("TC11", 3.0, "Kiểm tra chuyển hướng",
                    "Thành công", "Pass");
            Assert.assertTrue(driver.getCurrentUrl().contains("/login"));
        } catch (Exception e) {
            handleTestError("TC11", e);
        }
    }

    @AfterMethod
    public void afterMethod11() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }
    @Test(priority = 11, description = "TC14 - Mật khẩu quá dài")
    public void testSignupWithLongPassword() {
        try {
            openSignupTab();
            logPageInfo("TC14 - Mật khẩu dài");
            TestResultManager.put("TC14", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            String longPassword = new String(new char[51]).replace('\0', 'a');
            fillSignupFormWithWait("Nguyễn Văn M", "nguyenvanm@example.com",
                    longPassword, longPassword, "0987654325", "123 Đường ABC");
            TestResultManager.put("TC14", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            checkErrorMessageForField("Mật khẩu không được vượt quá 50 ký tự",
                    "passwordField", "TC14", 3.0);
        } catch (Exception e) {
            handleTestError("TC14", e);
        }
    }

    @AfterMethod
    public void afterMethod12() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 12, description = "TC15 - Tất cả trường trống")
    public void testSignupWithAllFieldsEmpty() {
        try {
            openSignupTab();
            logPageInfo("TC15 - Tất cả trống");
            TestResultManager.put("TC15", 1.0, "Mở tab đăng ký", "Thành công", "Pass");

            fillSignupFormWithWait(null, null, null, null, null, null);
            TestResultManager.put("TC15", 2.0, "Nhập dữ liệu", "Thành công", "Pass");

            clickSignupButton();

            // Kiểm tra multiple errors
            checkMultipleErrors(new String[]{
                    "Họ tên không được để trống",
                    "Email không được để trống",
                    "Mật khẩu không được để trống",
                    "Số điện thoại không được để trống"
            }, "TC15", 3.0);
        } catch (Exception e) {
            handleTestError("TC15", e);
        }
    }

    private void checkDoubleFieldError(String expectedMessage, String field1,
                                       String field2, String testCase, double step) {
        try {
            boolean error1 = driver.findElement(By.id(field1))
                    .getAttribute("validationMessage").contains(expectedMessage);
            boolean error2 = driver.findElement(By.id(field2))
                    .getAttribute("validationMessage").contains(expectedMessage);

            if (error1 && error2) {
                TestResultManager.put(testCase, step, "Kiểm tra lỗi", "Thành công", "Pass");
            } else {
                TestResultManager.put(testCase, step, "Kiểm tra lỗi", "Thất bại", "Fail");
                Assert.fail("Không hiển thị lỗi cho cả hai trường");
            }
        } catch (Exception e) {
            checkErrorMessageForField(expectedMessage, field1, testCase, step);
        }
    }

    @AfterMethod
    public void afterMethod13() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    private void checkMultipleErrors(String[] expectedMessages, String testCase, double step) {
        try {
            String pageSource = driver.getPageSource();
            int matchedErrors = 0;

            for (String msg : expectedMessages) {
                if (pageSource.contains(msg)) matchedErrors++;
            }

            if (matchedErrors == expectedMessages.length) {
                TestResultManager.put(testCase, step, "Kiểm tra nhiều lỗi", "Thành công", "Pass");
            } else {
                TestResultManager.put(testCase, step, "Kiểm tra nhiều lỗi",
                        "Thiếu " + (expectedMessages.length - matchedErrors) + " lỗi", "Fail");
                Assert.fail("Không hiển thị đủ thông báo lỗi");
            }
        } catch (Exception e) {
            TestResultManager.put(testCase, step, "Kiểm tra nhiều lỗi", "Lỗi hệ thống", "Fail");
            Assert.fail("Lỗi khi kiểm tra nhiều thông báo: " + e.getMessage());
        }
    }

    private void handleTestError(String testCase, Exception e) {
        System.err.println(testCase + " Error: " + e.getMessage());
        TestResultManager.put(testCase, 99.0, "Tổng kết", "Thất bại", "Fail");
        Assert.fail("Test " + testCase + " thất bại: " + e.getMessage());
    }

    @AfterClass
    public void tearDown() {
        try {
            // Lưu kết quả test vào file Excel
            TestResultManager.saveResults();
            System.out.println("Đã lưu kết quả test vào file Excel");
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu kết quả test: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}