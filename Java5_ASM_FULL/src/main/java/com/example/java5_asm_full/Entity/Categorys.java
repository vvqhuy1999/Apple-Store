package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "category")
public class Categorys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madanhmuc")
    private Integer madanhmuc;

    @Column(name = "tendanhmuc", unique = true, nullable = false)
    private String tendanhmuc;

    @Column(name = "mota")
    private String mota;

    @CreationTimestamp
    @Column(name = "ngaytao")
    private Date ngaytao;

    @OneToMany(mappedBy = "categorys")
    @JsonManagedReference
    private List<Products> products;
}
