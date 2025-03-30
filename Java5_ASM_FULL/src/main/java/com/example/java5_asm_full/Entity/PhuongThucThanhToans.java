package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "phuongthucthanhtoan")
public class PhuongThucThanhToans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maphuongthuc")
    private Integer maphuongthuc;

    @Column(name = "tenphuongthuc")
    private String tenphuongthuc;

    @OneToMany(mappedBy = "phuongthucthanhtoans", fetch = FetchType.LAZY)  // Sửa tên property
    @JsonManagedReference
    @JsonIgnore // Tránh vòng lặp JSON
    private List<Payments> payments = new ArrayList<>();
}
