package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "assess")
public class Assesss implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madanhgia")
    private Integer madanhgia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manguoidung")
    @JsonIgnore
    @JsonBackReference
    private Accounts accounts;  // Phải đổi tên này để khớp với mappedBy trong Accounts

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masanpham")
    @JsonIgnoreProperties({"assessments", "category", "detailproductmausacs"})  // Ignore các trường không cần thiết
    @JsonBackReference
    private Products products;

    @Column(name = "xephang")
    private Integer xephang;

    @Column(name = "binhluan", columnDefinition = "ntext")
    private String binhluan;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngaytao")
    private Date ngaytao;
}