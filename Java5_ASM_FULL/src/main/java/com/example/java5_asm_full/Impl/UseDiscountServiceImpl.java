package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.UseDiscountRepository;
import com.example.java5_asm_full.Entity.UseDiscounts;
import com.example.java5_asm_full.Service.UseDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UseDiscountServiceImpl implements UseDiscountService {
    private UseDiscountRepository useDiscountRepository;

    @Autowired
    public UseDiscountServiceImpl(UseDiscountRepository useDiscountRepository) {this.useDiscountRepository = useDiscountRepository;}

    @Override
    public List<UseDiscounts> findAll() {
        return useDiscountRepository.findAll();
    }

    @Override
    public UseDiscounts findById(int theID) {
        Optional<UseDiscounts> result = useDiscountRepository.findById(theID);
        UseDiscounts theUseDiscount = null;

        if (result.isPresent()){
            theUseDiscount = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find UseDiscount id - " +theID);
        }
        return theUseDiscount;
    }

    @Override
    public UseDiscounts save(UseDiscounts theUseDiscounts) {
        return useDiscountRepository.save(theUseDiscounts);
    }

    @Override
    public void deleteById(int theId) {
        useDiscountRepository.deleteById(theId);
    }
}
