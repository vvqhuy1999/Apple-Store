package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.PhuongThucThanhToanRepository;
import com.example.java5_asm_full.Entity.PhuongThucThanhToans;
import com.example.java5_asm_full.Service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PhuongThucThanhToanServiceImpl implements PhuongThucThanhToanService {
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    public PhuongThucThanhToanServiceImpl(PhuongThucThanhToanRepository phuongThucThanhToanRepository){this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;}

    @Transactional(readOnly = true)
    @Override
    public List<PhuongThucThanhToans> findAll() {
        return phuongThucThanhToanRepository.findAll();
    }

    @Override
    public PhuongThucThanhToans findById(int theID) {
        Optional<PhuongThucThanhToans> result = phuongThucThanhToanRepository.findById(theID);
        PhuongThucThanhToans thePhuongThucThanhToan = null;

        if (result.isPresent()){
            thePhuongThucThanhToan = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find PhuongThucThanhToan id - " +theID);
        }
        return thePhuongThucThanhToan;
    }

    @Override
    public PhuongThucThanhToans save(PhuongThucThanhToans thePhuongThucThanhToans) {
        return phuongThucThanhToanRepository.save(thePhuongThucThanhToans);    }

    @Override
    public void deleteById(int theId) {
        phuongThucThanhToanRepository.deleteById(theId);
    }

    public List<PhuongThucThanhToans> findAllBasic() {
        return phuongThucThanhToanRepository.findAllBasicInfo();
    }
}
