package org.example.abcbook.repository;

import org.example.abcbook.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepo extends JpaRepository<Suppliers, Long> {

}
