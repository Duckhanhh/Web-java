package org.example.abcbook.service;

import org.example.abcbook.model.Suppliers;
import org.example.abcbook.repository.SuppliersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {
    @Autowired
    private SuppliersRepo suppliersRepo;

    /**
     * Lay tat ca cac nxb
     * @return
     */
    public List<Suppliers> getAllSuppliers() {
        return suppliersRepo.findAll();
    }


}
