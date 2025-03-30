package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.java5_asm_full.Dao.AccountRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl (AccountRepository theAccountRepository){
        accountRepository = theAccountRepository;
    }

    @Override
    public List<Accounts> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Accounts findById(int theID) {
        Optional<Accounts> result = accountRepository.findById(theID);
        Accounts theAccount = null;

        if (result.isPresent()){
            theAccount = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Account id - " +theID);
        }
        return theAccount;
    }

    @Override
    public Accounts findByEmailAndPass(String email, String password) {
        Optional<Accounts> result = accountRepository.findByEmailAndPassword(email, password);
        Accounts theAccount = null;

        if (result.isPresent()){
            theAccount = result.get();
        }else {
            // we didn't find the employee
//            throw  new RuntimeException("Did not find Account id - " + email);
//            return false;
            theAccount = null;
        }
        return theAccount;
    }


    @Override
    public Accounts save(Accounts theAccount) {
        return accountRepository.save(theAccount);
    }

    @Override
    public void deleteById(int theId) {
        accountRepository.deleteById(theId);
    }
}
