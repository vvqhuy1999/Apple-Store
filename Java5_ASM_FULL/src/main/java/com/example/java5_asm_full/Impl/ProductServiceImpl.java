package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.ProductRepository;
import com.example.java5_asm_full.Entity.Products;
import com.example.java5_asm_full.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {this.productRepository = productRepository;}


    @Override
    public List<Products> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Products findById(int theID) {
        Optional<Products> result = productRepository.findById(theID);
        Products theProduct = null;

        if (result.isPresent()){
            theProduct = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Product id - " +theID);
        }
        return theProduct;
    }

    @Override
    public Products save(Products theProducts) {
        return productRepository.save(theProducts);
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }

    @Override
    public Page<Products> findAllPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Products> findByTensanphamContaining(String keyword, Pageable pageable) {
        return productRepository.findByTensanphamContaining(keyword, pageable);
    }
}
