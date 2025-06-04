package org.bpm.abcbook.service;


import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.BooksMapper;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.repository.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksMapper booksMapper;

    @Override
    public List<Inventory> getAllBookInStock() throws Exception {
        return inventoryRepo.findAll();
    }

    @Override
    public List<BookDTO> findBookInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                         Long bookFormat, List<String> listCategory, List<String> listSupplierCode, int rating,
                                         Long fromPrice, Long toPrice) throws Exception {
        return inventoryRepo.findAllInStock(insertUser, fromDate, toDate, statusInStock, listBookId, bookFormat, listCategory,
                listSupplierCode, rating, fromPrice, toPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBookInStock(BookDTO bookDTO) throws Exception {
        if (bookDTO == null || bookDTO.getId() == null) {
            throw new AppException("USBIS00001", "Thông tin sách không hợp lệ");
        }
        Long bookId = bookDTO.getId();
        Inventory inventory = inventoryRepo.findByBookId(bookId);
        inventory.setStatus(bookDTO.getBookStatusInStock());

        inventoryRepo.save(inventory);
    }
}
