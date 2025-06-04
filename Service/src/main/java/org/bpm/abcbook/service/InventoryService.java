package org.bpm.abcbook.service;

import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.model.Inventory;

import java.util.Date;
import java.util.List;

public interface InventoryService {
    List<Inventory> getAllBookInStock() throws Exception;

    List<BookDTO> findBookInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                  Long bookFormat, List<String> listCategory, List<String> listSupplierCode, int rating,
                                  Long fromPrice, Long toPrice) throws Exception;

    void updateStatusBookInStock(BookDTO bookDTO) throws Exception;
}
