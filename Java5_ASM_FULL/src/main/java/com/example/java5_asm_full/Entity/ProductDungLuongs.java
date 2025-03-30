package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "productdungluong")
public class ProductDungLuongs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maproductdungluong")
    private Integer maproductdungluong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masanpham", nullable = false)
    @JsonIgnoreProperties({"productdungluongs", "detailproductmausacs", "hinhanhs"})
    @JsonBackReference
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "madungluong", nullable = false)
    @JsonBackReference
    private DungLuongs dungluongs;

    @Column(name = "gia", nullable = false)
    private double gia;

    @Column(name = "soluongkho", nullable = false)
    private Integer soluongkho;

    @Override
    public String toString() {
        return "ProductDungLuongs [maproductdungluong= " + maproductdungluong + "]";
    }
}
