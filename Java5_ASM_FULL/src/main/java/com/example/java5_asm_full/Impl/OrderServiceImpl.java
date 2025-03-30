package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.OrderRepository;
import com.example.java5_asm_full.Entity.Orders;
import com.example.java5_asm_full.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository) {this.orderRepository = orderRepository;
    }

    @Override
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Orders findById(int theID) {
        Optional<Orders> result = orderRepository.findById(theID);
        Orders theOrder = null;

        if (result.isPresent()){
            theOrder = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Order id - " +theID);
        }
        return theOrder;
    }

    @Override
    public Orders save(Orders theOrders) {
        return orderRepository.save(theOrders);
    }

    @Override
    public void deleteById(int theId) {
        orderRepository.deleteById(theId);
    }
}
