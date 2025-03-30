package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.DungLuongRepository;
import com.example.java5_asm_full.Entity.DungLuongs;
import com.example.java5_asm_full.Service.DungLuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DungLuongServiceImpl implements DungLuongService {
    private DungLuongRepository dungLuongRepository;

    @Autowired
    public DungLuongServiceImpl(DungLuongRepository dlrepository) { this.dungLuongRepository = dlrepository; }


    @Override
    public List<DungLuongs> findAll() {
        return dungLuongRepository.findAll();
    }

    @Override
    public DungLuongs findById(int theID) {
        Optional<DungLuongs> result = dungLuongRepository.findById(theID);
        DungLuongs theDungLuong = null;

        if (result.isPresent()){
            theDungLuong = result.get();
        }else {
            // we didn't find the employee
            throw  new RuntimeException("Did not find Dung Luong id - " +theID);
        }
        return theDungLuong;
    }

    @Override
    public DungLuongs save(DungLuongs theDungLuongs) {
        return dungLuongRepository.save(theDungLuongs);
    }

    @Override
    public void deleteById(int theId) {
        dungLuongRepository.deleteById(theId);
    }
}
