package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepo extends JpaRepository<Orders, Long>, OrdersRepoExt {
    // Additional query methods can be defined here if needed
}
