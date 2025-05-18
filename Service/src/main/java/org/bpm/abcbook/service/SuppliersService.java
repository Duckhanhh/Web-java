package org.bpm.abcbook.service;

import org.bpm.abcbook.model.Suppliers;
import org.bpm.abcbook.repository.SuppliersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {
    @Autowired
    private SuppliersRepo suppliersRepo;

    /**
     * Ham lay tat ca nha cung cap
     *
     * @return
     */
    public List<Suppliers> findAllSuppliers() throws Exception {
        return suppliersRepo.findAll();
    }
}
