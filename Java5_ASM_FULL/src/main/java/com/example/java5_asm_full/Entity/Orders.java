package com.example.java5_asm_full.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "orderdetails", "usediscounts"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "madonhang"
)
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madonhang")
    private Integer madonhang;

    @ManyToOne
    @JoinColumn(name = "manguoidung")
    @JsonBackReference
    private Accounts accounts;

    @Column(name = "tongtien", nullable = false)
    private double tongtien;

    @Column(name = "trangthai")
    private Integer trangthai = 1;  // 0 la huy | 1 la gio hang | 2 don hang

    @CreationTimestamp
    @Column(name = "ngaydat")
    private Date ngaydat;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private List<OrderDetails> orderdetails = new ArrayList<>();  // Khởi tạo ArrayList

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<UseDiscounts> usediscounts = new ArrayList<>();

    @OneToOne(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payments payments;

    @OneToOne(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Ships ships;
}
