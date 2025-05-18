package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepo extends JpaRepository<Suppliers, Long>{
    // Custom query methods can be defined here if needed
}
