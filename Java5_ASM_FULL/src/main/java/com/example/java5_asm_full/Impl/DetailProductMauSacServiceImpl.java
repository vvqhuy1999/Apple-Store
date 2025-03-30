package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.DetailProductMauSacRepository;
import com.example.java5_asm_full.Entity.DetailProductMauSacs;
import com.example.java5_asm_full.Service.DetailProductMauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailProductMauSacServiceImpl implements DetailProductMauSacService {
    private DetailProductMauSacRepository detailProductMauSacRepository;

    @Autowired
    public DetailProductMauSacServiceImpl(DetailProductMauSacRepository repository) {this.detailProductMauSacRepository = repository;}


    @Override
    public List<DetailProductMauSacs> findAll() {
        return detailProductMauSacRepository.findAll();
    }

    @Override
    public DetailProductMauSacs findById(int theID) {
        Optional<DetailProductMauSacs> result = detailProductMauSacRepository.findById(theID);
        DetailProductMauSacs thedetailProductMauSac = null;

        if (result.isPresent()){
            thedetailProductMauSac = result.get();
        }else {
            throw  new RuntimeException("Did not find DetailProductMauSacRepository id - " +theID);
        }
        return thedetailProductMauSac;
    }

    @Override
    public DetailProductMauSacs save(DetailProductMauSacs theDetailProductMauSacs) {
        return detailProductMauSacRepository.save(theDetailProductMauSacs);
    }

    @Override
    public void deleteById(int theId) {
        detailProductMauSacRepository.deleteById(theId);
    }


    public List<DetailProductMauSacs> findDetailProductMauSacsByMasanpham(int theId) {
        return  detailProductMauSacRepository.getDetailProductMauSacsByMasanpham(theId);
    }
}
