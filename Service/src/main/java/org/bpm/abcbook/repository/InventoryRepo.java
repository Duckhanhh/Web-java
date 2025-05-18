package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    // Custom query methods can be defined here if needed
}
