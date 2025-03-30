package com.example.java5_asm_full.Entity;

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
@Table(name = "ship")
public class Ships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mavanchuyen")
    private Integer mavanchuyen;

    @OneToOne
    @JoinColumn(name = "madonhang")
    private Orders orders;

    @Column(name = "mavandon", unique = true)
    private String mavandon;

    @Column(name = "donvivanchuyen")
    private String donvivanchuyen;

    @Column(name = "trangthai")
    private Integer trangthai = 1;

    @Column(name = "ngaydukiengiao")
    private Date ngaydukiengiao;

    @CreationTimestamp
    @Column(name = "ngaygui")
    private Date ngaygui;
}
