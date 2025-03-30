package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "account")
public class Accounts implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manguoidung")
    private Integer manguoidung;

    @Column(name = "hoten", nullable = false, columnDefinition = "nvarchar(50)")
    private String hoten;

    @Column(name = "email", unique = true, nullable = false, columnDefinition = "varchar(50)")
    private String email;

    @Column(name = "matkhau", nullable = false, columnDefinition = "varchar(MAX)")
    private String matkhau;

    @Column(name = "sodienthoai", unique = true, nullable = false, columnDefinition = "varchar(15)")
    private String sodienthoai;

    @Column(name = "diachi", columnDefinition = "nvarchar(MAX)")
    private String diachi;

    @Column(name = "vaitro")
    private Integer vaitro = 2; // Mặc định là 0 (user thường)

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngaytao", updatable = false)
    private Date ngaytao;

    // Relationships
    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore   // tránh vòng lặp vô hạn khi gặp serialize JSON
    @JsonManagedReference
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<Assesss> assesses = new ArrayList<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<Discounts> discounts = new ArrayList<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<UseDiscounts> usediscounts = new ArrayList<>();

    @Override
    public String toString() {
        return "Accounts [manguoidung=" + manguoidung;
    }
}