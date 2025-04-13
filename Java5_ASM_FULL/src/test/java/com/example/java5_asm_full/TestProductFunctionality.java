package com.example.java5_asm_full;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestProductFunctionality {
    private WebDriver driver;
    private String baseUrl = "http://localhost:8080"; // URL
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // Explicit wait

        // Check if server is running before starting tests
        try {
            driver.get(baseUrl);
            System.out.println("Successfully connected to server: " + driver.getTitle());
            TestResultManager.put("Setup", 0.0, "Connect to server",
                    "Connection successful", "Pass");
        } catch (Exception e) {
            System.err.println("Cannot connect to server. Check URL and server status.");
            TestResultManager.put("Setup", 0.0, "Connect to server",
                    "Connection successful", "Fail");
            Assert.fail("Connection error: " + e.getMessage());
        }
    }

    // Helper method to wait for page to load
    private void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Helper method to log current page info (for debugging)
    private void logPageInfo(String testName) {
        System.out.println("--- " + testName + " ---");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
    }

    @Test(priority = 1, description = "TC17 - Hiển thị tất cả sản phẩm trang home")
    public void testDisplayAllProducts() {
        try {
            // Navigate to home page
            driver.get(baseUrl + "/items/index");
            waitForPageLoad();
            logPageInfo("TC17 - Hiển thị tất cả sản phẩm");
            TestResultManager.put("TC17", 1.0, "Open Home",
                    "Hiển thị trang home", "Pass");

            // Check if products are displayed
            List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class, 'card')]"));

            if (productCards.size() > 0) {
                System.out.println("Found " + productCards.size() + " products");

                // Check for product images, titles, and prices
                boolean hasImages = true;
                boolean hasTitles = true;
                boolean hasPrices = true;

                for (WebElement card : productCards) {
                    if (card.findElements(By.className("card-img-top")).isEmpty()) hasImages = false;
                    if (card.findElements(By.className("card-title")).isEmpty()) hasTitles = false;
                    if (card.findElements(By.className("price")).isEmpty()) hasPrices = false;
                }

                TestResultManager.put("TC17", 2.0, "Xác minh hiển thị sản phẩm",
                        "Sản phẩm được hiển thị với hình ảnh, tiêu đề và giá",
                        (hasImages && hasTitles && hasPrices) ? "Pass" : "Fail");

                Assert.assertTrue(hasImages && hasTitles && hasPrices,
                        "Sản phẩm cần được hiển thị với hình ảnh, tiêu đề và giá");
            } else {
                TestResultManager.put("TC17", 2.0, "Hiển thị sản phẩm",
                        "Không tìm thấy sản phẩm", "Fail");
                Assert.fail("No products found on the page");
            }
        } catch (Exception e) {
            System.err.println("TC17 Error: " + e.getMessage());
            TestResultManager.put("TC17", 3.0, "Tóm tắt trường hợp kiểm thử",
                    "Trường hợp kiểm thử đã hoàn thành", "Fail");
            Assert.fail("Test display all products failed: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod1() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test(priority = 2, description = "TC18 - Tìm kiếm sản phẩm theo từ khóa")
    public void testSearchProductsByKeyword() {
        try {
            // Chuyển đến trang chủ
            driver.get(baseUrl + "/items/index");
            waitForPageLoad();
            logPageInfo("TC18 - Tìm kiếm sản phẩm theo từ khóa");
            TestResultManager.put("TC18", 1.0, "Mở trang chủ",
                    "Trang chủ đã được hiển thị", "Pass");

            // Nhập từ khóa tìm kiếm
            String searchKeyword = "iphone";
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='keyword']")));
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("button-search")));

            searchInput.clear();
            searchInput.sendKeys(searchKeyword);
            TestResultManager.put("TC18", 2.0, "Nhập từ khóa tìm kiếm",
                    "Đã nhập từ khóa: " + searchKeyword, "Pass");

            // Nhấn nút tìm kiếm
            searchButton.click();
            waitForPageLoad();

            // Xác minh kết quả tìm kiếm
            List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class, 'card')]"));

            if (productCards.size() > 0) {
                System.out.println("Đã tìm thấy " + productCards.size() + " sản phẩm với từ khóa: " + searchKeyword);

                // Kiểm tra xem từ khóa có trong tiêu đề sản phẩm không
                boolean hasSearchTerm = true;
                List<WebElement> productTitles = driver.findElements(By.className("card-title"));

                for (WebElement title : productTitles) {
                    String titleText = title.getText().toLowerCase();
                    if (!titleText.contains(searchKeyword.toLowerCase())) {
                        hasSearchTerm = false;
                        System.out.println("Sản phẩm không khớp với từ khóa tìm kiếm: " + titleText);
                    }
                }

                TestResultManager.put("TC18", 3.0, "Xác minh kết quả tìm kiếm",
                        "Các sản phẩm chứa từ khóa '" + searchKeyword + "' đã được hiển thị",
                        hasSearchTerm ? "Pass" : "Fail");

                Assert.assertTrue(hasSearchTerm,
                        "Kết quả tìm kiếm nên chứa từ khóa: " + searchKeyword);
            } else {
                String pageSource = driver.getPageSource().toLowerCase();
                if (pageSource.contains("không tìm thấy") || pageSource.contains("no results")) {
                    TestResultManager.put("TC18", 3.0, "Xác minh kết quả tìm kiếm",
                            "Không tìm thấy sản phẩm cho từ khóa: " + searchKeyword, "Pass");
                } else {
                    TestResultManager.put("TC18", 3.0, "Xác minh kết quả tìm kiếm",
                            "Không có kết quả tìm kiếm rõ ràng hoặc thông báo lỗi", "Fail");
                    Assert.fail("Không có kết quả tìm kiếm rõ ràng hoặc thông báo lỗi cho từ khóa: " + searchKeyword);
                }
            }
        } catch (Exception e) {
            System.err.println("TC18 Lỗi: " + e.getMessage());
            TestResultManager.put("TC18", 4.0, "Tóm tắt trường hợp kiểm thử",
                    "Trường hợp kiểm thử đã hoàn thành", "Fail");
            Assert.fail("Kiểm thử tìm kiếm sản phẩm theo từ khóa thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod2() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test(priority = 3, description = "TC19 - Tìm kiếm với từ khóa không tồn tại")
    public void testSearchWithNonExistentKeyword() {
        try {
            // Chuyển đến trang chủ
            driver.get(baseUrl + "/items/index");
            waitForPageLoad();
            logPageInfo("TC19 - Tìm kiếm với từ khóa không tồn tại");
            TestResultManager.put("TC19", 1.0, "Mở trang chủ",
                    "Trang chủ đã được hiển thị", "Pass");

            // Nhập từ khóa tìm kiếm không tồn tại
            String searchKeyword = "xyzabc123";
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='keyword']")));
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("button-search")));

            searchInput.clear();
            searchInput.sendKeys(searchKeyword);
            TestResultManager.put("TC19", 2.0, "Nhập từ khóa không tồn tại",
                    "Đã nhập từ khóa: " + searchKeyword, "Pass");

            // Nhấn nút tìm kiếm
            searchButton.click();
            waitForPageLoad();

            // Xác minh thông báo không có kết quả
            List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class, 'card')]"));

            if (productCards.size() == 0) {
                // Kiểm tra thông báo "không tìm thấy sản phẩm"
                String pageSource = driver.getPageSource().toLowerCase();
                boolean hasNoResultsMsg = pageSource.contains("không tìm thấy") ||
                        pageSource.contains("không có sản phẩm") ||
                        pageSource.contains("no products") ||
                        pageSource.contains("no results");

                TestResultManager.put("TC19", 3.0, "Xác minh thông báo không có kết quả",
                        "Thông báo không có kết quả đã được hiển thị", hasNoResultsMsg ? "Pass" : "Fail");

                Assert.assertTrue(hasNoResultsMsg,
                        "Phải hiển thị thông báo 'không tìm thấy sản phẩm' cho từ khóa tìm kiếm không tồn tại");
            } else {
                TestResultManager.put("TC19", 3.0, "Xác minh không có sản phẩm",
                        "Đã tìm thấy sản phẩm một cách bất ngờ", "Fail");
                Assert.fail("Đã tìm thấy " + productCards.size() +
                        " sản phẩm khi tìm kiếm với từ khóa không tồn tại: " + searchKeyword);
            }
        } catch (Exception e) {
            System.err.println("TC19 Lỗi: " + e.getMessage());
            TestResultManager.put("TC19", 4.0, "Tóm tắt trường hợp kiểm thử",
                    "Trường hợp kiểm thử đã hoàn thành", "Fail");
            Assert.fail("Kiểm thử tìm kiếm với từ khóa không tồn tại thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod3() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test(priority = 4, description = "TC20 - Lọc sản phẩm theo danh mục")
    public void testFilterProductsByCategory() {
        try {
            // Chuyển đến trang chủ
            driver.get(baseUrl + "/items/index");
            waitForPageLoad();
            logPageInfo("TC20 - Lọc sản phẩm theo danh mục");
            TestResultManager.put("TC20", 1.0, "Mở trang chủ",
                    "Trang chủ đã được hiển thị", "Pass");

            // Tìm và nhấp vào một danh mục - đang tìm "Ipad" làm ví dụ
            String category = "Ipad";

            // Thử các phương pháp khác nhau để tìm lựa chọn danh mục
            try {
                // Thử tìm một dropdown danh mục trước
                WebElement categoryDropdown = driver.findElement(By.cssSelector("select.form-control"));
                Select select = new Select(categoryDropdown);
                select.selectByVisibleText(category);
                TestResultManager.put("TC20", 2.0, "Chọn danh mục từ dropdown",
                        "Danh mục đã được chọn: " + category, "Pass");
            } catch (Exception e) {
                // Nếu không tìm thấy dropdown, thử tìm một liên kết danh mục
                try {
                    WebElement categoryLink = driver.findElement(By.linkText(category));
                    categoryLink.click();
                    TestResultManager.put("TC20", 2.0, "Nhấp vào liên kết danh mục",
                            "Danh mục đã được nhấp: " + category, "Pass");
                } catch (Exception e2) {
                    // Thử một cách tiếp cận khác - tìm liên kết chứa văn bản danh mục
                    try {
                        WebElement categoryLink = driver.findElement(
                                By.xpath("//a[contains(text(), '" + category + "')]"));
                        categoryLink.click();
                        TestResultManager.put("TC20", 2.0, "Nhấp vào liên kết danh mục (xpath)",
                                "Danh mục đã được nhấp: " + category, "Pass");
                    } catch (Exception e3) {
                        TestResultManager.put("TC20", 2.0, "Tìm công cụ chọn danh mục",
                                "Công cụ chọn danh mục không tìm thấy", "Fail");
                        Assert.fail("Không thể tìm thấy công cụ chọn danh mục cho: " + category);
                    }
                }
            }

            waitForPageLoad();

            // Xác minh sản phẩm đã được lọc theo danh mục
            List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class, 'card')]"));

            if (productCards.size() > 0) {
                System.out.println("Đã tìm thấy " + productCards.size() + " sản phẩm trong danh mục: " + category);

                // Kiểm tra tiêu đề trang hoặc tiêu đề để xác minh rằng chúng ta đang xem kết quả đã được lọc
                boolean isCategoryPage = driver.getTitle().contains(category) ||
                        driver.getPageSource().contains("Category: " + category) ||
                        driver.getPageSource().contains("Danh mục: " + category);

                TestResultManager.put("TC20", 3.0, "Xác minh sản phẩm đã được lọc",
                        "Sản phẩm đã được lọc theo danh mục: " + category,
                        isCategoryPage ? "Pass" : "Fail");

                Assert.assertTrue(isCategoryPage,
                        "Phải hiển thị sản phẩm đã được lọc theo danh mục: " + category);
            } else {
                // Có thể đúng khi không có sản phẩm trong một danh mục
                TestResultManager.put("TC20", 3.0, "Xác minh sản phẩm đã được lọc",
                        "Không tìm thấy sản phẩm trong danh mục: " + category, "Pass");
            }
        } catch (Exception e) {
            System.err.println("TC20 Lỗi: " + e.getMessage());
            TestResultManager.put("TC20", 4.0, "Tóm tắt trường hợp kiểm thử",
                    "Trường hợp kiểm thử đã hoàn thành", "Fail");
            Assert.fail("Kiểm thử lọc sản phẩm theo danh mục thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod4() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test(priority = 5, description = "TC21 - Sắp xếp sản phẩm theo giá")
    public void testSortProductsByPrice() {
        try {
            // Chuyển đến trang chủ
            driver.get(baseUrl + "/items/index");
            waitForPageLoad();
            logPageInfo("TC21 - Sắp xếp sản phẩm theo giá");
            TestResultManager.put("TC21", 1.0, "Mở trang chủ",
                    "Trang chủ đã được hiển thị", "Pass");

            // Thử tìm tùy chọn sắp xếp - đang tìm sắp xếp theo giá
            try {
                // Thử tìm một dropdown sắp xếp
                WebElement sortDropdown = driver.findElement(By.xpath("//select[contains(@name, 'sort')]"));
                Select select = new Select(sortDropdown);
                select.selectByValue("price_asc"); // Giả sử "price_asc" là giá trị cho giá tăng dần
                TestResultManager.put("TC21", 2.0, "Chọn sắp xếp theo giá (tăng dần)",
                        "Tùy chọn sắp xếp đã được chọn", "Pass");
            } catch (Exception e) {
                // Thử tìm một nút liên kết sắp xếp theo giá
                try {
                    WebElement sortLink = driver.findElement(By.xpath("//a[contains(text(), 'Giá: Thấp đến cao')]"));
                    sortLink.click();
                    TestResultManager.put("TC21", 2.0, "Nhấp vào liên kết sắp xếp theo giá",
                            "Tùy chọn sắp xếp đã được nhấp", "Pass");
                } catch (Exception e2) {
                    // Nếu không tìm thấy giao diện sắp xếp rõ ràng, kiểm thử có thể thất bại
                    TestResultManager.put("TC21", 2.0, "Tìm tùy chọn sắp xếp",
                            "Tùy chọn sắp xếp không tìm thấy", "Fail");
                    Assert.fail("Không thể tìm thấy tùy chọn sắp xếp theo giá");
                }
            }

            waitForPageLoad();

            // Xác minh sản phẩm đã được sắp xếp theo giá (tăng dần)
            List<WebElement> priceElements = driver.findElements(By.className("price"));

            if (priceElements.size() > 1) {
                // Lấy giá và kiểm tra xem chúng có được sắp xếp không
                boolean isSorted = true;
                double prevPrice = -1;

                for (WebElement priceElem : priceElements) {
                    String priceText = priceElem.getText().replaceAll("[^0-9]", ""); // Xóa các ký tự không phải số
                    double price = Double.parseDouble(priceText);

                    if (prevPrice != -1 && price < prevPrice) {
                        isSorted = false;
                        break;
                    }

                    prevPrice = price;
                }

                TestResultManager.put("TC21", 3.0, "Xác minh sản phẩm đã được sắp xếp theo giá",
                        "Sản phẩm đã được sắp xếp theo giá tăng dần", isSorted ? "Pass" : "Fail");

                Assert.assertTrue(isSorted, "Sản phẩm nên được sắp xếp theo giá theo thứ tự tăng dần");
            } else {
                TestResultManager.put("TC21", 3.0, "Xác minh sản phẩm đã được sắp xếp",
                        "Không đủ sản phẩm để xác minh việc sắp xếp", "Pass");
            }
        } catch (Exception e) {
            System.err.println("TC21 Lỗi: " + e.getMessage());
            TestResultManager.put("TC21", 4.0, "Tóm tắt trường hợp kiểm thử",
                    "Trường hợp kiểm thử đã hoàn thành", "Fail");
            Assert.fail("Kiểm thử sắp xếp sản phẩm theo giá thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod5() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @Test(priority = 10, description = "TC22 - Phân trang")
    public void testPagination() {
        try {
            // Bước 1: Mở trang phân trang với page=0
            String page0Url = baseUrl + "/items/index?page=0&size=6&keyword=";
            System.out.println("TC22 - Mở URL trang 0: " + page0Url);
            driver.get(page0Url);
            waitForPageLoad();

            // Bước 2: Lưu và kiểm tra danh sách sản phẩm ở trang 0
            List<WebElement> productsPage0 = driver.findElements(
                    By.xpath("//div[contains(@class,'col-md-4') and contains(@class,'col-sm-6')]"));
            int productsCountPage0 = productsPage0.size();
            System.out.println("TC22 - Số lượng sản phẩm trên trang 0: " + productsCountPage0);

            // Kiểm tra có sản phẩm trên trang 0
            if (productsCountPage0 == 0) {
                TestResultManager.put("TC22", 1.0, "Kiểm tra nội dung trang 0",
                        "Không tìm thấy sản phẩm nào trên trang 0", "Fail");
                Assert.fail("Không tìm thấy sản phẩm nào trên trang 0");
                return;
            }

            // Ghi nhận kết quả thành công
            TestResultManager.put("TC22", 1.0, "Kiểm tra nội dung trang 0",
                    "Tìm thấy " + productsCountPage0 + " sản phẩm trên trang 0", "Pass");

            // Lưu tên các sản phẩm trên trang 0 để so sánh sau
            List<String> productNamesPage0 = new ArrayList<>();
            for (WebElement product : productsPage0) {
                try {
                    WebElement productNameElement = product.findElement(
                            By.xpath(".//h5[contains(@class,'card-title')] | .//div[contains(@class,'product-title')] | .//div[@class='ten']"));
                    String productName = productNameElement.getText().trim();
                    productNamesPage0.add(productName);
                } catch (Exception e) {
                    // Nếu không tìm thấy tên sản phẩm, bỏ qua
                    System.out.println("TC22 - Không tìm thấy tên cho một sản phẩm trên trang 0");
                }
            }

            // In ra tên sản phẩm đầu tiên để debug
            if (!productNamesPage0.isEmpty()) {
                System.out.println("TC22 - Sản phẩm đầu tiên trên trang 0: " + productNamesPage0.get(0));
            }

            // Bước 3: Chuyển đến trang 1 bằng cách thay đổi URL trực tiếp
            String page1Url = baseUrl + "/items/index?page=1&size=6&keyword=";
            System.out.println("TC22 - Chuyển đến URL trang 1: " + page1Url);
            driver.get(page1Url);
            waitForPageLoad();

            // Bước 4: Kiểm tra URL đã chuyển đúng sang trang 1
            String currentUrl = driver.getCurrentUrl();
            System.out.println("TC22 - URL hiện tại: " + currentUrl);

            boolean isPage1 = currentUrl.contains("page=1");
            if (isPage1) {
                TestResultManager.put("TC22", 2.0, "Kiểm tra chuyển trang",
                        "Chuyển sang trang 1 thành công", "Pass");
            } else {
                TestResultManager.put("TC22", 2.0, "Kiểm tra chuyển trang",
                        "Chuyển sang trang 1 thất bại", "Fail");
                Assert.fail("URL không chứa 'page=1' sau khi chuyển trang");
                return;
            }

            // Bước 5: Kiểm tra danh sách sản phẩm ở trang 1
            List<WebElement> productsPage1 = driver.findElements(
                    By.xpath("//div[contains(@class,'col-md-4') and contains(@class,'col-sm-6')]"));
            int productsCountPage1 = productsPage1.size();
            System.out.println("TC22 - Số lượng sản phẩm trên trang 1: " + productsCountPage1);

            // Kiểm tra có sản phẩm trên trang 1
            if (productsCountPage1 == 0) {
                // Nếu không có sản phẩm trên trang 1, kiểm tra xem có phải trang cuối không
                if (productsCountPage0 < 6) {
                    TestResultManager.put("TC22", 3.0, "Kiểm tra nội dung trang 1",
                            "Không có sản phẩm trên trang 1 do tổng số sản phẩm ít hơn kích thước trang", "Pass");
                    return;
                } else {
                    TestResultManager.put("TC22", 3.0, "Kiểm tra nội dung trang 1",
                            "Không tìm thấy sản phẩm nào trên trang 1 mặc dù có dữ liệu", "Fail");
                    Assert.fail("Không tìm thấy sản phẩm nào trên trang 1 mặc dù cần phân trang");
                    return;
                }
            }

            // Bước 6: Lưu tên sản phẩm trên trang 1 để so sánh
            List<String> productNamesPage1 = new ArrayList<>();
            for (WebElement product : productsPage1) {
                try {
                    WebElement productNameElement = product.findElement(
                            By.xpath(".//h5[contains(@class,'card-title')] | .//div[contains(@class,'product-title')] | .//div[@class='ten']"));
                    String productName = productNameElement.getText().trim();
                    productNamesPage1.add(productName);
                } catch (Exception e) {
                    // Nếu không tìm thấy tên sản phẩm, bỏ qua
                    System.out.println("TC22 - Không tìm thấy tên cho một sản phẩm trên trang 1");
                }
            }

            // In ra tên sản phẩm đầu tiên trên trang 1 để debug
            if (!productNamesPage1.isEmpty()) {
                System.out.println("TC22 - Sản phẩm đầu tiên trên trang 1: " + productNamesPage1.get(0));
            }

            // Bước 7: Kiểm tra xem sản phẩm trên hai trang có khác nhau không
            boolean allProductsDifferent = true;
            for (String productNamePage1 : productNamesPage1) {
                if (productNamesPage0.contains(productNamePage1)) {
                    allProductsDifferent = false;
                    System.out.println("TC22 - Phát hiện sản phẩm trùng lặp: " + productNamePage1);
                    break;
                }
            }

            if (allProductsDifferent) {
                TestResultManager.put("TC22", 3.0, "Kiểm tra nội dung giữa các trang",
                        "Các sản phẩm trên trang 0 và trang 1 khác nhau hoàn toàn, phân trang hoạt động đúng", "Pass");
            } else {
                // Nếu có trùng lặp nhưng vẫn có sản phẩm khác, xem như chấp nhận được
                if (!productNamesPage0.get(0).equals(productNamesPage1.get(0))) {
                    TestResultManager.put("TC22", 3.0, "Kiểm tra nội dung giữa các trang",
                            "Sản phẩm đầu tiên trên hai trang khác nhau, phân trang hoạt động đúng", "Pass");
                } else {
                    TestResultManager.put("TC22", 3.0, "Kiểm tra nội dung giữa các trang",
                            "Sản phẩm đầu tiên trên hai trang giống nhau, phân trang có thể không hoạt động đúng", "Fail");
                    Assert.fail("Phân trang không hoạt động đúng: sản phẩm đầu tiên trên cả hai trang giống nhau");
                }
            }

            // Bước 8: Tổng kết test case
            TestResultManager.put("TC22", 4.0, "Tổng kết test case",
                    "Test case phân trang hoàn tất thành công", "Pass");

        } catch (Exception e) {
            System.err.println("TC22 Error: " + e.getMessage());
            e.printStackTrace();
            TestResultManager.put("TC22", 4.0, "Tổng kết test case",
                    "Test case chạy hoàn tất với lỗi: " + e.getMessage(), "Fail");
            Assert.fail("Test phân trang thất bại: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod6() {
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }

    @AfterClass
    public void tearDown() {
        try {
            // Save test results to Excel file
            TestResultManager.saveResults();
            System.out.println("Test results saved to Excel file");
        } catch (Exception e) {
            System.err.println("Error saving test results: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}