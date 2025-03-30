package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.DiscountRepository;
import com.example.java5_asm_full.Entity.Discounts;
import com.example.java5_asm_full.Service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {
    private DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {this.discountRepository = discountRepository;}


    @Override
    public List<Discounts> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discounts findById(int theID) {
        Optional<Discounts> result = discountRepository.findById(theID);
        Discounts theDiscount = null;

        if (result.isPresent()){
            theDiscount = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Discount id - " +theID);
        }
        return theDiscount;
    }

    @Override
    public Discounts save(Discounts theDiscounts) {
        return discountRepository.save(theDiscounts);
    }

    @Override
    public void deleteById(int theId) {
        discountRepository.deleteById(theId);
    }
}
