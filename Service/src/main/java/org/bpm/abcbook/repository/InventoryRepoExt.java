package org.bpm.abcbook.repository;

import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.model.Inventory;

import java.util.Date;
import java.util.List;

public interface InventoryRepoExt {
    List<BookDTO> findAllInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                 Long bookFormat, List<String> listCategory, List<String> listSupplierCode, int rating,
                                 Long fromPrice, Long toPrice);

    Inventory findByBookId(Long bookId) throws Exception;
}
