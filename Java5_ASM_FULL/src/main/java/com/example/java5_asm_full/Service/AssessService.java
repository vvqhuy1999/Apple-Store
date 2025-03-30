package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Entity.Assesss;

import java.util.List;

public interface AssessService {
    List<Assesss> findAll ();

    Assesss findById (int theID);

    Assesss save (Assesss theAssess);

    void deleteById(int theId);
}
