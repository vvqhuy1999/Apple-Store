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
@Table(name = "hinhanh")
public class HinhAnhs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mahinhanh")
    private Integer mahinhanh;

    @Column(name = "linkhinhanh")
    private String linkhinhanh;

    @ManyToOne
    @JoinColumn(name = "masanpham")
    @JsonBackReference
    private Products products;

    @ManyToOne
    @JoinColumn(name = "mamausac")
    @JsonBackReference
    private MauSacs mausacs;

    @Override
    public String toString() {
        return "HinhAnhs [mahinhanh=" + mahinhanh + ", linkhinhanh=" + linkhinhanh + "]";
    }
}
