package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.HinhAnhRepository;
import com.example.java5_asm_full.Entity.HinhAnhs;
import com.example.java5_asm_full.Service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class HinhAnhServiceImpl implements HinhAnhService {
    private HinhAnhRepository hinhAnhRepository;
    @Autowired
    public HinhAnhServiceImpl(HinhAnhRepository hinhAnhRepository) {this.hinhAnhRepository = hinhAnhRepository;}


    @Override
    public List<HinhAnhs> findAll() {
        return hinhAnhRepository.findAll();
    }

    @Override
    public HinhAnhs findById(int theID) {
        Optional<HinhAnhs> result = hinhAnhRepository.findById(theID);
        HinhAnhs theHinhanh = null;

        if (result.isPresent()){
            theHinhanh = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Hinh Anh id - " +theID);
        }
        return theHinhanh;
    }

    @Override
    public HinhAnhs save(HinhAnhs theHinhAnhs) {
        return hinhAnhRepository.save(theHinhAnhs);
    }

    @Override
    public void deleteById(int theId) {
        hinhAnhRepository.deleteById(theId);
    }


    public List<HinhAnhs> findOneForProduct(int theId) {
        Pageable pageable = (Pageable) PageRequest.of(0, 1);
        return hinhAnhRepository.getFirstImageForProduct(theId, pageable);
    }

    public List<HinhAnhs> findMaSanPham(int theId) {
        return hinhAnhRepository.getMaSanPham(theId);
    }
}
