package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.PaymentRepository;
import com.example.java5_asm_full.Entity.Payments;
import com.example.java5_asm_full.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {this.paymentRepository = paymentRepository;}


    @Override
    public List<Payments> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payments findById(int theID) {
        Optional<Payments> result = paymentRepository.findById(theID);
        Payments thePayment = null;

        if (result.isPresent()){
            thePayment = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Payment id - " +theID);
        }
        return thePayment;
    }

    @Override
    public Payments save(Payments thePayments) {
        return paymentRepository.save(thePayments);
    }

    @Override
    public void deleteById(int theId) {
        paymentRepository.deleteById(theId);
    }
}
