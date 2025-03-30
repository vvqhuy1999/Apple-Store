package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "orderdetail")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machitietdonhang")
    private Integer machitietdonhang;

    @ManyToOne
    @JoinColumn(name = "madonhang")
    @JsonBackReference
    @JsonIgnore
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "masanpham")
    @JsonBackReference
    @JsonIgnore
    private Products products;

    @Column(name = "soluong", nullable = false)
    private Integer soluong;

    @Column(name = "gia", nullable = false)
    private double gia;

    //  thêm reference đến ProductDungLuongs:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maproductdungluong")
    @JsonBackReference
    @JsonIgnore
    private ProductDungLuongs productDungLuongs;
}
