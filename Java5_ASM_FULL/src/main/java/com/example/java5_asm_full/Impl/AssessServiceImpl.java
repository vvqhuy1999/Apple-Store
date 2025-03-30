package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.AccountRepository;
import com.example.java5_asm_full.Dao.AssessRepository;
import com.example.java5_asm_full.Entity.Accounts;
import com.example.java5_asm_full.Entity.Assesss;
import com.example.java5_asm_full.Service.AccountService;
import com.example.java5_asm_full.Service.AssessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssessServiceImpl implements AssessService {
    private AssessRepository assessRepository;

    @Autowired
    public AssessServiceImpl(AssessRepository assessRepository){
        this.assessRepository = assessRepository;
    }


    @Override
    public List<Assesss> findAll() {
        return assessRepository.findAll();
    }

    @Override
    public Assesss findById(int theID) {
        Optional<Assesss> result = assessRepository.findById(theID);
        Assesss theAssess = null;

        if (result.isPresent()){
            theAssess = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Assess id - " +theID);
        }
        return theAssess;
    }

    @Override
    public Assesss save(Assesss theAssess) {
        return assessRepository.save(theAssess);
    }

    @Override
    public void deleteById(int theId) {
        assessRepository.deleteById(theId);
    }


}
