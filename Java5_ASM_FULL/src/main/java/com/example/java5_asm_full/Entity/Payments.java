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
@Table(name = "payment")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mathanhtoan")
    private Integer mathanhtoan;

    @OneToOne
    @JoinColumn(name = "madonhang")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "maphuongthuc")
    @JsonBackReference
    private PhuongThucThanhToans phuongthucthanhtoans;  // Tên property này đã đúng

    @Column(name = "trangthaithanhtoan")
    private Integer trangthaithanhtoan = 1;

    @Column(name = "sotienthanhtoan", nullable = false)
    private double sotienthanhtoan;

    @CreationTimestamp
    @Column(name = "ngaythanhtoan")
    private Date ngaythanhtoan;
}
