package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.OrderDetailRepository;
import com.example.java5_asm_full.Entity.OrderDetails;
import com.example.java5_asm_full.Service.OrderDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(
            OrderDetailRepository orderDetailRepository) {this.orderDetailRepository = orderDetailRepository;
    }


    @Override
    public List<OrderDetails> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetails findById(int theID) {
        Optional<OrderDetails> result = orderDetailRepository.findById(theID);
        OrderDetails theOrderDetail = null;

        if (result.isPresent()){
            theOrderDetail = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find OrderDetail id - " +theID);
        }
        return theOrderDetail;
    }

    @Override
    public OrderDetails save(OrderDetails theOrderDetails) {
        return orderDetailRepository.save(theOrderDetails);
    }

    @Override
    public void deleteById(int theId) {
        orderDetailRepository.deleteById(theId);
    }
}
