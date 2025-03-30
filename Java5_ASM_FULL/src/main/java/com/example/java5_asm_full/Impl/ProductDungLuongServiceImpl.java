package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.ProductDungLuongRepository;
import com.example.java5_asm_full.Entity.HinhAnhs;
import com.example.java5_asm_full.Entity.ProductDungLuongs;
import com.example.java5_asm_full.Service.ProductDungLuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDungLuongServiceImpl implements ProductDungLuongService {
    private ProductDungLuongRepository productDungLuongRepository;
    @Autowired
    public ProductDungLuongServiceImpl(ProductDungLuongRepository productDungLuongRepository) {this.productDungLuongRepository = productDungLuongRepository;}

    @Override
    public List<ProductDungLuongs> findAll() {
        return productDungLuongRepository.findAll();
    }

    @Override
    public ProductDungLuongs findById(int theID) {
        Optional<ProductDungLuongs> result = productDungLuongRepository.findById(theID);
        ProductDungLuongs theProductDungLuong = null;

        if (result.isPresent()){
            theProductDungLuong = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find ProductDungLuong id - " +theID);
        }
        return theProductDungLuong;
    }

    @Override
    public ProductDungLuongs save(ProductDungLuongs theProductDungLuongs) {
        return productDungLuongRepository.save(theProductDungLuongs);
    }

    @Override
    public void deleteById(int theId) {
        productDungLuongRepository.deleteById(theId);
    }

    public List<ProductDungLuongs> findOneForGiaProduct(int theId) {
        Pageable pageable = (Pageable) PageRequest.of(0, 1);
        return productDungLuongRepository.getFirstGiaProduct(theId, pageable);
    }

    public List<ProductDungLuongs> findMaSanPham(int theId) {
        return productDungLuongRepository.getMaSanPhamforPoducDungLuong(theId);
    }

}
