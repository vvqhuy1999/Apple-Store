package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "product")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "masanpham")
    private Integer masanpham;

    @Column(name = "tensanpham", nullable = false)
    private String tensanpham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "madanhmuc", nullable = false)
    @JsonIgnoreProperties("products")
    @JsonBackReference
    private Categorys categorys;

    @Column(name = "mota", columnDefinition = "NTEXT")
    private String mota;

    @CreationTimestamp
    @Column(name = "ngaytao")
    private Date ngaytao;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("products")
    @JsonManagedReference
    private List<ProductDungLuongs> productdungluongs = new ArrayList<>();

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("products")
    @JsonManagedReference
    private List<DetailProductMauSacs> detailproductmausacs = new ArrayList<>();

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("products")
    @JsonManagedReference
    private List<HinhAnhs> hinhanhs = new ArrayList<>();

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("products")
    @JsonManagedReference
    private List<Assesss> assesss = new ArrayList<>();

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("products")
    @JsonManagedReference
    private List<OrderDetails> orderdetails = new ArrayList<>();

    @Override
    public String toString() {
        return "Products [masanpham=" + masanpham + ", tensanpham=" + tensanpham + "]";
    }
}
