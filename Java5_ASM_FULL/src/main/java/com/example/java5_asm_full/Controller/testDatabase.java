package com.example.java5_asm_full.Controller;

import com.example.java5_asm_full.Entity.*;
import com.example.java5_asm_full.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/database")
public class testDatabase {
    private final AccountService accountService;
    private final AssessService assessService;
    private final CategoryService categoryService;
    private final DetailProductMauSacService detailProductMauSacService;
    private final DiscountService discountService;
    private final DungLuongService dungLuongService;
    private final HinhAnhService hinhAnhService;
    private final MauSacService mauSacService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PhuongThucThanhToanService phuongThucThanhToanService;
    private final ProductDungLuongService productDungLuongService;
    private final ProductService productService;
    private final ShipService shipService;
    private final UseDiscountService useDiscountService;

    @Autowired
    public testDatabase(
            AccountService accountService,
            AssessService assessService,
            CategoryService categoryService,
            DetailProductMauSacService detailProductMauSacService,
            DiscountService discountService,
            DungLuongService dungLuongService,
            HinhAnhService hinhAnhService,
            MauSacService mauSacService,
            OrderDetailService orderDetailService,
            OrderService orderService,
            PaymentService paymentService,
            PhuongThucThanhToanService phuongThucThanhToanService,
            ProductDungLuongService productDungLuongService,
            ProductService productService,
            ShipService shipService,
            UseDiscountService useDiscountService) {
        this.accountService = accountService;
        this.assessService = assessService;
        this.categoryService = categoryService;
        this.detailProductMauSacService = detailProductMauSacService;
        this.discountService = discountService;
        this.dungLuongService = dungLuongService;
        this.hinhAnhService = hinhAnhService;
        this.mauSacService = mauSacService;
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.phuongThucThanhToanService = phuongThucThanhToanService;
        this.productDungLuongService = productDungLuongService;
        this.productService = productService;
        this.shipService = shipService;
        this.useDiscountService = useDiscountService;
    }


    @GetMapping("/accounts")
    public List<Accounts> findAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/assesses")
    public List<Assesss> findAllAssesses() {
        return assessService.findAll();
    }

    @GetMapping("/categories")
    public List<Categorys> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/detail-products")
    public List<DetailProductMauSacs> findAllDetailProducts() {
        return detailProductMauSacService.findAll();
    }

        @GetMapping("/discounts")
    public List<Discounts> findAllDiscounts() {
        return discountService.findAll();
    }

    @GetMapping("/dungluongs")
    public List<DungLuongs> findAllDungLuongs() {
        return dungLuongService.findAll();
    }

    @GetMapping("/hinhanhs")
    public List<HinhAnhs> findAllHinhAnhs() {
        return hinhAnhService.findAll();
    }

    @GetMapping("/mausacs")
    public List<MauSacs> findAllMauSacs() {
        return mauSacService.findAll();
    }

    @GetMapping("/order-details")
    public List<OrderDetails> findAllOrderDetails() {
        return orderDetailService.findAll();
    }

    @GetMapping("/orders")
    public List<Orders> findAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/payments")
    public List<Payments> findAllPayments() {
        return paymentService.findAll();
    }

    @GetMapping("/phuongthucthanhtoan")
    public List<PhuongThucThanhToans> findAllPhuongThucThanhToans() {
        return phuongThucThanhToanService.findAll();
    }

    @GetMapping("/product-dungluongs")
    public List<ProductDungLuongs> findAllProductDungLuongs() {
        return productDungLuongService.findAll();
    }

    @GetMapping("/products")
    public List<Products> findAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/ships")
    public List<Ships> findAllShips() {
        return shipService.findAll();
    }

    @GetMapping("/use-discounts")
    public List<UseDiscounts> findAllUseDiscounts() {
        return useDiscountService.findAll();
    }
}
