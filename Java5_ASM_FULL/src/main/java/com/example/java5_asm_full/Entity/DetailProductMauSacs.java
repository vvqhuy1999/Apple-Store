package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "detailproductmausac")
public class DetailProductMauSacs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madetailproductmausac")
    private Integer madetailproductmausac;

    @ManyToOne
    @JoinColumn(name = "mamausac")
    @JsonBackReference
    private MauSacs mausacs;

    @ManyToOne
    @JoinColumn(name = "masanpham")
    @JsonBackReference
    private Products products;

    @CreationTimestamp
    @Column(name = "ngaytao")
    private Date ngaytao;

    @Override
    public String toString() {
        return "DetailProductMauSacs [madetailproductmausac=" + madetailproductmausac + ", ngaytao=" + ngaytao + "]";
    }
}
