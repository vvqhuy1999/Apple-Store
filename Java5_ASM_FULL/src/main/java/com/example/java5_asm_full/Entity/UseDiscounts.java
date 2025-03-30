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
@Table(name = "usediscount")
public class UseDiscounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mausediscount")
    private Integer mausediscount;

    @ManyToOne
    @JoinColumn(name = "manguoidung")
    @JsonBackReference
    private Accounts accounts;

    @ManyToOne
    @JoinColumn(name = "madiscount")
    @JsonBackReference
    private Discounts discounts;

    @CreationTimestamp
    @Column(name = "ngaysudung")
    private Date ngaysudung;

    @ManyToOne
    @JoinColumn(name = "madonhang")
    @JsonBackReference
    private Orders orders;
}
