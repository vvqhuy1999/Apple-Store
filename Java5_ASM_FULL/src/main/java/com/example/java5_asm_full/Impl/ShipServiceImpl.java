package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.ShipRepository;
import com.example.java5_asm_full.Entity.Ships;
import com.example.java5_asm_full.Service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {
    private ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {this.shipRepository = shipRepository;}

    @Override
    public List<Ships> findAll() {
        return shipRepository.findAll();
    }

    @Override
    public Ships findById(int theID) {
        Optional<Ships> result = shipRepository.findById(theID);
        Ships theShip = null;

        if (result.isPresent()){
            theShip = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Ship id - " +theID);
        }
        return theShip;
    }

    @Override
    public Ships save(Ships theShips) {
        return shipRepository.save(theShips);
    }

    @Override
    public void deleteById(int theId) {
        shipRepository.deleteById(theId);
    }
}
