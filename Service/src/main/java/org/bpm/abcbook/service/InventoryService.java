package org.bpm.abcbook.service;


import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.repository.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    List<Inventory> getAllBookInStock() {
        return inventoryRepo.findAll();
    }
}
