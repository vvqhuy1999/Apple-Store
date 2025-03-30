package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mausac")
public class MauSacs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mamausac")
    private Integer mamausac;

    @Column(name = "englishname")
    private String englishname;

    @Column(name = "tenmausac", nullable = false)
    private String tenmausac;

    //    @OneToMany(mappedBy = "mausac")
//    private List<DetailProductMauSac> detailProductMauSacs;
    @OneToMany(mappedBy = "mausacs", fetch = FetchType.LAZY)  // Chú ý tên property phải khớp
    @JsonManagedReference
    private List<DetailProductMauSacs> detailproductmausacs = new ArrayList<>();

    @OneToMany(mappedBy = "mausacs")
    @JsonManagedReference
    private List<HinhAnhs> hinhanhs;
}
