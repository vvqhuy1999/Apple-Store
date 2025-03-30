package com.example.java5_asm_full.Controller;

import com.example.java5_asm_full.Entity.*;
import com.example.java5_asm_full.Impl.DetailProductMauSacServiceImpl;
import com.example.java5_asm_full.Impl.ProductDungLuongServiceImpl;
import org.springframework.ui.Model;
import com.example.java5_asm_full.Impl.HinhAnhServiceImpl;
import com.example.java5_asm_full.Service.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.*;

import java.util.stream.Collectors;

@Controller
public class ItemsController {
    private static final Logger logger = LoggerFactory.getLogger(ItemsController.class);
    @Autowired
    CookieService cookieService;

    @Autowired
    ParamService paramService;

    @Autowired
    SessionService sessionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;


    @Autowired
    ProductDungLuongServiceImpl productDungLuongService;
    @Autowired
    DungLuongService dungLuongService;
    @Autowired
    DetailProductMauSacServiceImpl detailProductMauSacService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    HinhAnhServiceImpl hinhAnhService;


    @GetMapping("/items/index")
    public String DSSanPham(Model model,
                            @PageableDefault(page = 0, size = 6, sort = "masanpham", direction = Sort.Direction.ASC) Pageable pageable)
    {
        // Lấy danh sách sản phẩm phân trang từ productService
        Page<Products> productPage = productService.findAllPage(pageable);

        DecimalFormat df = new DecimalFormat("#,###");
        List<Products> resultProduct = productService.findAll() != null ? productService.findAll() : Collections.emptyList();

        // Chuyển đổi Page<Products> thành Page<ProductDTO>
        Page<ProductDTO> productDTOPage = productPage.map(p -> {
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
            return new ProductDTO(p.getMasanpham(), p.getTensanpham(), hinhAnhLinks, giaFormatted);
        });

//        productDTOs.forEach(dto ->
//                System.out.println("SanPham: " + dto.getTensanpham() + ", HinhAnhLinks: " + dto.getHinhAnhLinks() + ", Gia: " + dto.getGia()));

        model.addAttribute("products", productDTOPage.getContent());
        model.addAttribute("page", productDTOPage); // Thêm đối tượng Page để sử dụng trong view
        model.addAttribute("keyword", null); // Đặt keyword là null cho danh sách thông thường
        return "items/index";
    }

    @GetMapping("/items/search")
    public String searchProducts(@RequestParam("keyword") String keyword,
                                 Model model,
                                 @PageableDefault(page = 0, size = 6, sort = "masanpham", direction = Sort.Direction.ASC) Pageable pageable) {
        DecimalFormat df = new DecimalFormat("#,###");

        // Tìm kiếm sản phẩm dựa trên từ khóa (giả sử tìm theo tên sản phẩm)
        Page<Products> productPage = productService.findByTensanphamContaining(keyword, pageable);

        Page<ProductDTO> productDTOPage = productPage.map(p -> {
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
            return new ProductDTO(p.getMasanpham(), p.getTensanpham(), hinhAnhLinks, giaFormatted);
        });

        model.addAttribute("products", productDTOPage.getContent());
        model.addAttribute("page", productDTOPage);
        model.addAttribute("keyword", keyword); // Giữ từ khóa tìm kiếm để hiển thị lại trong input
        return "items/index";
    }


    @RequestMapping("/items/detail/{id}")
    public String viewDetail(@PathVariable("id") Integer id, Model model) {
        logger.debug("Requesting product detail for ID: {}", id);

        Products product = productService.findById(id);
        if (product == null) {
            logger.error("Product with ID {} not found", id);
            model.addAttribute("error", "Không tìm thấy sản phẩm với ID: " + id);
            return "items/detail";
        }

        try {
            // Lấy danh sách hình ảnh
            List<HinhAnhs> hinhAnhs = Optional.ofNullable(hinhAnhService.findMaSanPham(id))
                    .orElse(new ArrayList<>());

            // Lấy danh sách dung lượng và giá
            List<ProductDungLuongs> productDungLuongs = Optional.ofNullable(productDungLuongService.findMaSanPham(id))
                    .orElse(new ArrayList<>());

            // Lấy danh sách màu sắc và xử lý trùng lặp
            List<DetailProductMauSacs> detailProductMauSacs = Optional.ofNullable(
                            detailProductMauSacService.findDetailProductMauSacsByMasanpham(id))
                    .orElse(new ArrayList<>());

            // Tạo map để lưu trữ màu sắc và hình ảnh tương ứng
            Map<Integer, List<String>> mauSacHinhAnhMap = new HashMap<>();
            for (HinhAnhs hinhAnh : hinhAnhs) {
                Integer maMauSac = hinhAnh.getMausacs().getMamausac();
                mauSacHinhAnhMap.computeIfAbsent(maMauSac, k -> new ArrayList<>())
                        .add(hinhAnh.getLinkhinhanh());
            }

            // Lọc màu sắc trùng lặp
            Set<MauSacs> mauSacsSet = detailProductMauSacs.stream()
                    .map(DetailProductMauSacs::getMausacs)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            List<MauSacs> mauSacsList = new ArrayList<>(mauSacsSet);

            // Xử lý dung lượng
            Set<DungLuongs> dungLuongsSet = productDungLuongs.stream()
                    .map(ProductDungLuongs::getDungluongs)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            List<DungLuongs> dungLuongsList = new ArrayList<>(dungLuongsSet);

            // Định dạng giá
            DecimalFormat df = new DecimalFormat("#,###");
            List<String> giaFormatted = productDungLuongs.stream()
                    .map(pd -> df.format(pd.getGia()))
                    .collect(Collectors.toList());

            // Thêm dữ liệu vào model
            model.addAttribute("product", product);
            model.addAttribute("hinhAnhs", hinhAnhs);
            model.addAttribute("mauSacHinhAnhMap", mauSacHinhAnhMap); // Thêm map màu sắc-hình ảnh
            model.addAttribute("productDungLuongs", productDungLuongs);
            model.addAttribute("giaFormatted", giaFormatted);
            model.addAttribute("detailProductMauSacs", detailProductMauSacs);
            model.addAttribute("mauSacs", mauSacsList);
            model.addAttribute("dungLuongs", dungLuongsList);

        } catch (Exception e) {
            logger.error("Error while processing product detail: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi xử lý thông tin sản phẩm");
            return "items/detail";
        }

        return "items/detail";
    }





    // DTO đơn giản
    @Data
    public static class ProductDTO {
        private Integer masanpham;
        private String tensanpham;
        private List<String> hinhAnhLinks;
        private String gia;

        public ProductDTO(Integer masanpham, String tensanpham, List<String> hinhAnhLinks, String gia) {
            this.masanpham = masanpham;
            this.tensanpham = tensanpham;
            this.hinhAnhLinks = hinhAnhLinks;
            this.gia = gia;
        }


    }

}
