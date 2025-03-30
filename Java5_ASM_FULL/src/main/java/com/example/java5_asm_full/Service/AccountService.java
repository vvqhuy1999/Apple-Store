package com.example.java5_asm_full.Service;

import com.example.java5_asm_full.Entity.Accounts;

import java.util.List;

public interface AccountService {
    List<Accounts> findAll ();

    Accounts findById (int theID);

    Accounts save (Accounts theAccount);

    void deleteById(int theId);

    Accounts findByEmailAndPass (String email, String password);
}
