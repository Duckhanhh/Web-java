package org.bpm.abcbook.repository;

import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.model.Inventory;

import java.util.Date;
import java.util.List;

public interface InventoryRepoExt {
    List<BookDTO> findAllInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                 Long bookFormat, List<String> listCategory, List<String> listSupplierCode, Integer rating,
                                 Long fromPrice, Long toPrice, List<String> listAuthor);

    Inventory findByBookId(Long bookId) throws Exception;

    List<Inventory> findAllByListBookId(List<Long> listBookId) throws Exception;
}
