package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Ships;

import java.util.List;

public interface ShipService {
    List<Ships> findAll ();

    Ships findById (int theID);

    Ships save (Ships theShips);

    void deleteById(int theId);
}
