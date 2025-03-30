package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Discounts;
import com.example.java5_asm_full.Entity.DungLuongs;

import java.util.List;

public interface DungLuongService {
    List<DungLuongs> findAll ();

    DungLuongs findById (int theID);

    DungLuongs save (DungLuongs theDungLuongs);

    void deleteById(int theId);
}
