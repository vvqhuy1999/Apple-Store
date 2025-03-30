package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "discount")
public class Discounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madiscount")
    private Integer madiscount;

    @Column(name = "macoupon")
    private String macoupon;

    @Column(name = "sl")
    private Integer sl;

    @Column(name = "phantram")
    private Float phantram;

    @CreationTimestamp
    @Column(name = "ngaytao")
    private Date ngaytao;

    @Column(name = "ngayhethan")
    private Date ngayhethan;

    @ManyToOne
    @JoinColumn(name = "manguoidung")
    @JsonBackReference
    private Accounts accounts;

    @OneToMany(mappedBy = "discounts")
    @JsonManagedReference
    private List<UseDiscounts> usediscounts;
}
