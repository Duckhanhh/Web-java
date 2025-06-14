package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long>, InventoryRepoExt {
    @Modifying
    @Query("UPDATE Inventory i SET i.updateDate = CURRENT_TIMESTAMP, i.updateUser = :updateUser, i.quantity = i.quantity + :addedQuantity WHERE i.id = :bookId")
    void increaseQuantity(@Param("bookId") Long bookId, @Param("addedQuantity") Long addedQuantity, @Param("updateUser") String updateUser);
}
