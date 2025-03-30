package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.MauSacRepository;
import com.example.java5_asm_full.Entity.MauSacs;
import com.example.java5_asm_full.Service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacServiceImpl implements MauSacService {
    private MauSacRepository mauSacRepository;

    @Autowired
    public MauSacServiceImpl(MauSacRepository mauSacRepository) {this.mauSacRepository = mauSacRepository;}



    @Override
    public List<MauSacs> findAll() {
        return mauSacRepository.findAll();
    }

    @Override
    public MauSacs findById(int theID) {
        Optional<MauSacs> result = mauSacRepository.findById(theID);
        MauSacs theMauSac = null;

        if (result.isPresent()){
            theMauSac = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Mau Sac id - " +theID);
        }
        return theMauSac;
    }

    @Override
    public MauSacs save(MauSacs theMauSacs) {
        return mauSacRepository.save(theMauSacs);
    }

    @Override
    public void deleteById(int theId) {
        mauSacRepository.deleteById(theId);
    }
}
