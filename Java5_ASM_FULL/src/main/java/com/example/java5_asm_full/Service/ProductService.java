package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.ProductDungLuongs;
import com.example.java5_asm_full.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ProductService {
    List<Products> findAll ();

    Products findById (int theID);

    Products save (Products theProducts);

    void deleteById(int theId);

    Page<Products> findAllPage(Pageable pageable);

    Page<Products> findByTensanphamContaining(String keyword, Pageable pageable);
}
