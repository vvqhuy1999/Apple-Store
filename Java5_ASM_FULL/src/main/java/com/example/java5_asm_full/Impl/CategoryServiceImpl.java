package com.example.java5_asm_full.Impl;

import com.example.java5_asm_full.Dao.CategoryRepository;
import com.example.java5_asm_full.Entity.Categorys;
import com.example.java5_asm_full.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {this.categoryRepository = categoryRepository;}


    @Override
    public List<Categorys> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Categorys findById(int theID) {
        Optional<Categorys> result = categoryRepository.findById(theID);
        Categorys theCategory = null;

        if (result.isPresent()){
            theCategory = result.get();
        }else {
            throw  new RuntimeException("Did not find Category id - " +theID);
        }
        return theCategory;
    }

    @Override
    public Categorys save(Categorys theCategorys) {
        return categoryRepository.save(theCategorys);
    }

    @Override
    public void deleteById(int theId) {
        categoryRepository.deleteById(theId);
    }
}
