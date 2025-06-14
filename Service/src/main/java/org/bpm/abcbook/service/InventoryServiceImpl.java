package org.bpm.abcbook.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.dto.BookImportDTO;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.BooksMapper;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.repository.BooksRepo;
import org.bpm.abcbook.repository.InventoryRepo;
import org.bpm.abcbook.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private BooksRepo booksRepo;

    @Override
    public List<Inventory> getAllBookInStock() throws Exception {
        return inventoryRepo.findAll();
    }

    @Override
    public List<BookDTO> findBookInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                         Long bookFormat, List<String> listCategory, List<String> listSupplierCode, int rating,
                                         Long fromPrice, Long toPrice, List<String> listAuthor) throws Exception {
        return inventoryRepo.findAllInStock(insertUser, fromDate, toDate, statusInStock, listBookId, bookFormat, listCategory,
                listSupplierCode, rating, fromPrice, toPrice, listAuthor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBookInStock(BookDTO bookDTO, String updateStaff) throws Exception {
        if (bookDTO == null || bookDTO.getId() == null) {
            throw new AppException("USBIS00001", "Thông tin sách không hợp lệ");
        }
        Long bookId = bookDTO.getId();
        Inventory inventory = inventoryRepo.findByBookId(bookId);
        inventory.setStatus(bookDTO.getBookStatusInStock());

        inventory.setUpdateUser(updateStaff);
        inventory.setUpdateDate(new Date());

        inventoryRepo.save(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBook(List<BookImportDTO> importDTOList, String staffCode) throws Exception {
        if (DataUtil.isNullOrEmpty(importDTOList)) {
            throw new AppException("IB00001", "Phải có dữ liệu sách");
        }

        List<Long> listBookId = importDTOList.stream().map(BookImportDTO::getBookId).toList();
        List<Long> foundIds = booksRepo.findAllById(listBookId)
                .stream()
                .map(Books::getId)
                .toList();

        if (foundIds.size() != listBookId.size()) {
            throw new AppException("IB00002", "Thông tin id sách bị sai");
        }

        for (BookImportDTO item : importDTOList) {
            if (item == null) {
                continue;
            }

            inventoryRepo.increaseQuantity(item.getBookId(), item.getQuantity(), staffCode);
        }
    }
}
