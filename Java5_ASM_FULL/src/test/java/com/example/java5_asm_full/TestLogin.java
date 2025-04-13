package com.example.java5_asm_full;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestLogin {
    private WebDriver driver;
    private String baseUrl = "http://localhost:8080"; //  URL
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // Explicit wait với thời gian dài hơn

        // Kiểm tra xem server có hoạt động không trước khi bắt đầu test
        try {
            driver.get(baseUrl);
            System.out.println("Kết nối đến server thành công: " + driver.getTitle());
//            TestResultManager.put("Setup", 0.0, "Kết nối đến server",
//                    "Kết nối thành công", "Pass");
        } catch (Exception e) {
            System.err.println("Không thể kết nối đến server. Kiểm tra lại URL và tình trạng server.");
//            TestResultManager.put("Setup", 0.0, "Kết nối đến server",
//                    "Kết nối thành công", "Fail");
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

    @Test(priority = 1, description = "TC013 - Kiểm tra đăng nhập thất bại với mật khẩu không đúng")
    public void testLoginFailWithIncorrectPassword() {
        try {
            // Mở trang đăng nhập
            driver.get(baseUrl + "/account/login");
            waitForPageLoad();
            logPageInfo("TC013 - Đăng nhập sai mật khẩu");
            TestResultManager.put("TC013", 1.0, "Mở trang đăng nhập",
                    "Trang đăng nhập hiển thị", "Pass");

            // Nhập email đúng nhưng mật khẩu sai
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginEmail")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginPassword")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));

            emailField.clear();
            emailField.sendKeys("user1@gmail.com");
            passwordField.clear();
            passwordField.sendKeys("wrong_password");

            // Nhấn nút đăng nhập ngay lập tức (không cần sleep)
            loginButton.click();

            // Sử dụng wait ngắn hơn cho việc kiểm tra thông báo lỗi
            try {
                WebElement errorMessage = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//h5[@class='text-danger text-center mt-3']")));

                boolean hasErrorMessage = errorMessage.isDisplayed() &&
                        (errorMessage.getText().contains("Mật khẩu không chính xác") ||
                                errorMessage.getText().contains("không đúng") ||
                                errorMessage.getText().contains("Invalid"));

                System.out.println("Error message: " + errorMessage.getText());

                if (hasErrorMessage) {
                    TestResultManager.put("TC013", 2.0, "Kiểm tra thông báo lỗi",
                            "Hiển thị thông báo lỗi mật khẩu", "Pass");
                    Assert.assertTrue(hasErrorMessage, "Không hiển thị thông báo lỗi khi nhập sai mật khẩu");
                } else {
                    TestResultManager.put("TC013", 2.0, "Kiểm tra thông báo lỗi",
                            "Hiển thị thông báo lỗi mật khẩu", "Fail");
                    Assert.fail("Không hiển thị thông báo lỗi khi nhập sai mật khẩu");
                }
            } catch (Exception e) {
                // Kiểm tra nhanh chóng trong page source
                String pageSource = driver.getPageSource();
                boolean containsErrorText = pageSource.contains("Mật khẩu không chính xác") ||
                        pageSource.contains("không đúng") ||
                        pageSource.contains("Invalid password");

                if (containsErrorText) {
                    TestResultManager.put("TC013", 2.0, "Kiểm tra thông báo lỗi trong page source",
                            "Hiển thị thông báo lỗi mật khẩu", "Pass");
                } else {
                    TestResultManager.put("TC013", 2.0, "Kiểm tra thông báo lỗi trong page source",
                            "Hiển thị thông báo lỗi mật khẩu", "Fail");
                    Assert.fail("Không tìm thấy thông báo lỗi sau khi đăng nhập sai mật khẩu");
                }
            }
        } catch (Exception e) {
            System.err.println("TC013 Error: " + e.getMessage());
            TestResultManager.put("TC013", 3.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng nhập thất bại với mật khẩu không đúng thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod1() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 2, description = "TC14 - Kiểm tra đăng nhập với email sai định dạng")
    public void testLoginWithInvalidEmailFormat() {
        try {
            // Mở trang đăng nhập
            driver.get(baseUrl + "/account/login");
            waitForPageLoad();
            logPageInfo("TC14 - Đăng nhập với email sai định dạng");
            TestResultManager.put("TC14", 1.0, "Mở trang đăng nhập",
                    "Trang đăng nhập hiển thị", "Pass");

            // Nhập email sai định dạng
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginEmail")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginPassword")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));

            emailField.clear();
            emailField.sendKeys("user1gmail.com"); // Thiếu @
            passwordField.clear();
            passwordField.sendKeys("123456");
//            TestResultManager.put("TC14", 2.0, "Nhập email sai định dạng",
//                    "Form nhập liệu hoạt động", "Pass");

            // Thread.sleep(2000) để chờ 2 giây
            Thread.sleep(2000);

            // Nhấn nút đăng nhập và kiểm tra kết quả
            loginButton.click();

            // Kiểm tra cả hai khả năng: HTML5 validation hoặc validation từ server
            try {
                // Kiểm tra xem có thông báo lỗi HTML5 không
                boolean hasHtml5Validation = driver.findElements(By.xpath("//input[@id='loginEmail']:invalid")).size() > 0;

                if (hasHtml5Validation) {
                    TestResultManager.put("TC14", 2.0, "Kiểm tra HTML5 validation",
                            "Hiển thị lỗi HTML5 cho email sai định dạng", "Pass");
                    Assert.assertTrue(hasHtml5Validation, "Không hiển thị lỗi HTML5 validation khi nhập email sai định dạng");
                } else {
                    // Nếu không có HTML5 validation, kiểm tra thông báo lỗi từ server
                    waitForPageLoad();
                    WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h5[@class='text-danger text-center mt-3']")));

                    boolean hasErrorMessage = errorMessage.isDisplayed() &&
                            errorMessage.getText().contains("Email không hợp lệ");

                    System.out.println("Error message: " + errorMessage.getText());

                    if (hasErrorMessage) {
                        TestResultManager.put("TC14", 2.0, "Kiểm tra thông báo lỗi từ server",
                                "Hiển thị thông báo lỗi email không hợp lệ", "Pass");
                        Assert.assertTrue(hasErrorMessage, "Không hiển thị thông báo lỗi khi nhập email sai định dạng");
                    } else {
                        TestResultManager.put("TC14", 2.0, "Kiểm tra thông báo lỗi từ server",
                                "Hiển thị thông báo lỗi email không hợp lệ", "Fail");
                        Assert.fail("Không hiển thị thông báo lỗi khi nhập email sai định dạng");
                    }
                }
            } catch (Exception e) {
                // Kiểm tra nếu thông báo lỗi xuất hiện trong page source
                String pageSource = driver.getPageSource();
                boolean containsErrorText = pageSource.contains("Email không hợp lệ") ||
                        pageSource.contains("invalid email");

                if (containsErrorText) {
                    TestResultManager.put("TC14", 2.0, "Kiểm tra thông báo lỗi trong page source",
                            "Hiển thị thông báo lỗi email không hợp lệ", "Pass");
                    Assert.assertTrue(containsErrorText,
                            "Không tìm thấy thông báo lỗi email không hợp lệ trong page source");
                } else {
                    TestResultManager.put("TC14", 2.0, "Kiểm tra thông báo lỗi trong page source",
                            "Hiển thị thông báo lỗi email không hợp lệ", "Fail");
                    Assert.fail("Không tìm thấy thông báo lỗi email không hợp lệ trong page source");
                }
            }
        } catch (Exception e) {
            System.err.println("TC14 Error: " + e.getMessage());
            TestResultManager.put("TC14", 3.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng nhập với email sai định dạng thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod2() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }

    @Test(priority = 3, description = "TC15 - Kiểm tra để trống trường bắt buộc khi đăng nhập")
    public void testLoginWithEmptyFields() {
        try {
            // Mở trang đăng nhập
            driver.get(baseUrl + "/account/login");
            waitForPageLoad();
            logPageInfo("TC15 - Đăng nhập với trường trống");
            TestResultManager.put("TC15", 1.0, "Mở trang đăng nhập",
                    "Trang đăng nhập hiển thị", "Pass");

            // Để trống các trường
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginEmail")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginPassword")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));

            emailField.clear();
            passwordField.clear();
            TestResultManager.put("TC15", 2.0, "Xóa dữ liệu các trường",
                    "Các trường đã trống", "Pass");

            // Thread.sleep(2000) để chờ 2 giây
            Thread.sleep(2000);

            // Nhấn nút đăng nhập
            loginButton.click();

            // Kiểm tra HTML5 validation trước
            boolean emailInvalid = driver.findElements(By.cssSelector("#loginEmail:invalid")).size() > 0;
            boolean passwordInvalid = driver.findElements(By.cssSelector("#loginPassword:invalid")).size() > 0;
            boolean formValidationActive = emailInvalid || passwordInvalid;

            // Kiểm tra thêm thuộc tính required để biết form đang validate
            boolean emailRequired = isAttributePresent(emailField, "required");
            boolean passwordRequired = isAttributePresent(passwordField, "required");

            // Kết hợp tất cả điều kiện
            boolean validationActive = formValidationActive || emailRequired || passwordRequired;

            if (formValidationActive) {
                TestResultManager.put("TC15", 3.0, "Kiểm tra HTML5 validation",
                        "HTML5 validation ngăn form submit", "Pass");
                System.out.println("HTML5 validation đã ngăn form submit khi các trường bắt buộc trống");
                Assert.assertTrue(formValidationActive, "Form không validate các trường bắt buộc");
            } else {
                // Nếu không có HTML5 validation, kiểm tra thông báo lỗi từ server
                waitForPageLoad();
                try {
                    WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h5[@class='text-danger text-center mt-3']")));

                    boolean hasErrorMessage = errorMessage.isDisplayed() &&
                            (errorMessage.getText().contains("không được để trống") ||
                                    errorMessage.getText().contains("required"));

                    System.out.println("Error message: " + errorMessage.getText());

                    if (hasErrorMessage) {
                        TestResultManager.put("TC15", 3.0, "Kiểm tra thông báo lỗi từ server",
                                "Hiển thị thông báo lỗi trường bắt buộc", "Pass");
                        Assert.assertTrue(hasErrorMessage, "Không hiển thị thông báo lỗi khi để trống các trường");
                    } else {
                        TestResultManager.put("TC15", 3.0, "Kiểm tra thông báo lỗi từ server",
                                "Hiển thị thông báo lỗi trường bắt buộc", "Fail");
                        Assert.fail("Không hiển thị thông báo lỗi khi để trống các trường");
                    }
                } catch (Exception e) {
                    // Kiểm tra nếu thông báo lỗi xuất hiện trong page source
                    String pageSource = driver.getPageSource();
                    boolean containsErrorText = pageSource.contains("không được để trống") ||
                            pageSource.contains("required field");

                    if (containsErrorText) {
                        TestResultManager.put("TC15", 3.0, "Kiểm tra thông báo lỗi trong page source",
                                "Hiển thị thông báo lỗi trường bắt buộc", "Pass");
                        Assert.assertTrue(containsErrorText,
                                "Không tìm thấy thông báo lỗi trường bắt buộc trong page source");
                    } else {
                        TestResultManager.put("TC15", 3.0, "Kiểm tra thông báo lỗi trong page source",
                                "Hiển thị thông báo lỗi trường bắt buộc", "Fail");
                        Assert.fail("Không tìm thấy thông báo lỗi trường bắt buộc trong page source");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("TC15 Error: " + e.getMessage());
            TestResultManager.put("TC15", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng nhập với các trường trống thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod3() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
    }
    @Test(priority = 4, description = "TC16 - Kiểm tra đăng nhập thành công")
    public void testLoginSuccess() {
        try {
            // Mở trang đăng nhập
            driver.get(baseUrl + "/account/login");
            waitForPageLoad();
            logPageInfo("TC16 - Đăng nhập thành công");
            TestResultManager.put("TC16", 1.0, "Mở trang đăng nhập",
                    "Trang đăng nhập hiển thị", "Pass");

            // Tìm và xác nhận form đăng nhập tồn tại
            WebElement loginForm = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//form[contains(@action, '/account/login')]")));
            Assert.assertTrue(loginForm.isDisplayed(), "Form đăng nhập không hiển thị");
//            TestResultManager.put("TC16", 2.0, "Kiểm tra form đăng nhập",
//                    "Form đăng nhập hiển thị", "Pass");

            // Nhập thông tin đăng nhập hợp lệ
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginEmail")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginPassword")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng Nhập')]")));

            emailField.clear();
            emailField.sendKeys("user1@gmail.com");
            passwordField.clear();
            passwordField.sendKeys("123456");
//            TestResultManager.put("TC16", 3.0, "Nhập thông tin đăng nhập hợp lệ",
//                    "Thông tin đăng nhập đã được nhập", "Pass");

            // Thread.sleep(2000) để chờ 2 giây
            Thread.sleep(2000);

            // Nhấn nút đăng nhập
            loginButton.click();
            waitForPageLoad();

            // Lấy URL sau khi đăng nhập để kiểm tra kết quả
            String currentUrl = driver.getCurrentUrl();
            logPageInfo("Sau khi đăng nhập");

            // Kiểm tra chuyển hướng đến trang items/index sau khi đăng nhập thành công
            boolean isLoggedIn = currentUrl.contains("/items/index");

            if (isLoggedIn) {
                TestResultManager.put("TC16", 2.0, "Kiểm tra chuyển hướng sau đăng nhập",
                        "Chuyển đến trang /items/index", "Pass");
                Assert.assertTrue(isLoggedIn, "Đăng nhập không thành công - URL sau đăng nhập: " + currentUrl);
            } else {
                TestResultManager.put("TC16", 2.0, "Kiểm tra chuyển hướng sau đăng nhập",
                        "Chuyển đến trang /items/index", "Fail");
                Assert.fail("Đăng nhập không thành công - URL sau đăng nhập: " + currentUrl);
            }
        } catch (Exception e) {
            System.err.println("TC16 Error: " + e.getMessage());
            TestResultManager.put("TC16", 3.0, "Tổng kết test case",
                    "Test case chạy hoàn tất", "Fail");
            Assert.fail("Test đăng nhập thành công thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod4() {
        // Xóa cache nếu cần
        driver.manage().deleteAllCookies();

        // Hoặc thậm chí refresh trình duyệt
        driver.navigate().refresh();
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