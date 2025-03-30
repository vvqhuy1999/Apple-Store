package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "dungluong")
public class DungLuongs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madungluong")
    private Integer madungluong;

    @Column(name = "sodungluong")
    private Integer sodungluong;

    @Column(name = "donvi")
    private String donvi = "GB";

    @OneToMany(mappedBy = "dungluongs")
    @JsonManagedReference
    private List<ProductDungLuongs> productdungluongs;

    @Override
    public String toString() {
        return "DungLuongs [madungluong=" + madungluong + ", sodungluong=" + sodungluong + "donvi="+ donvi + "]";
    }
}
