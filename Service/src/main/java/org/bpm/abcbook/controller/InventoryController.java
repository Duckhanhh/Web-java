package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.Const;
import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.dto.response.StaffResponse;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Suppliers;
import org.bpm.abcbook.service.*;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.internal.CoreLogging.logger;

@Named
@Data
@ViewScoped
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private List<BookDTO> resultList;
    private List<Books> bookList;
    private List<Long> listBookSelected;
    private Long bookFormat; //dinh dang sach
    private List<String> listAuthor; //danh sach tac gia
    private List<String> listSelectedAuthor; //danh sach tac gia duoc chon
    private List<String> listCategory; //danh sach the loai
    private List<String> listSelectedCategory; //danh sach the loai duoc chon
    private List<Suppliers> suppliersList; //danh sach nha cung cap
    private List<String> listSelectedCodeSupplier; //danh sach nha cung cap duoc chon
    private Long status; //trang thai sach trong kho
    private List<StaffResponse> listStaff; //danh sach nhan vien
    private List<String> listSelectedStaff; //danh sach nhan vien duoc chon
    private List<Date> listSelectedDate; //danh sach ngay nhap kho
    private int rating; //danh gia
    private List<Books> listBookInStock; //danh sach sach trong kho
    private Long fromPrice; //gia tu
    private Long toPrice; //gia den
    private Map<String, String> supplierLabels; // nhãn cho nhà cung cấp
    private BookDTO selectedBook; // sách được chọn
    private String currentStaff; // mã nhân viên hiện tại

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private SuppliersService suppliersService;
    @Autowired
    private StaffService staffService;
    @Inject
    private UserSessionBean userSessionBean;

    @PostConstruct
    public void init() {
        //Lay ra danh sach sach
        try {
            clearDataSearch();
            bookList = booksService.findAllBook();
            listAuthor = booksService.getAllAuthor();
            listCategory = booksService.getAllCategory();
            suppliersList = suppliersService.findAllSuppliers();
            listStaff = staffService.getAllStaffs();

            supplierLabels = suppliersList.stream().collect(Collectors.toMap(Suppliers::getSupplierCode, Suppliers::getSupplierName));

            currentStaff = userSessionBean == null || userSessionBean.getCurrentStaff() == null ? null : userSessionBean.getCurrentStaff().getStaffCode();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
    }

    public void clearDataSearch() {
        status = null;
        bookFormat = null;
        rating = 0;
        fromPrice = null;
        toPrice = null;
        listBookSelected = new ArrayList<>();
        listSelectedAuthor = new ArrayList<>();
        listSelectedCategory = new ArrayList<>();
        listSelectedCodeSupplier = new ArrayList<>();
        listSelectedStaff = new ArrayList<>();
        listSelectedDate = new ArrayList<>();
        resultList = new ArrayList<>();
    }

    public void searchInventory() {
        try {
            Date fromDate = null;
            Date toDate = null;
            if (listSelectedDate != null && !listSelectedDate.isEmpty()) {
                fromDate = listSelectedDate.getFirst();
                toDate = listSelectedDate.getLast();
            }

            resultList = inventoryService.findBookInStock(listSelectedStaff, fromDate, toDate, status, listBookSelected,
                    bookFormat, listSelectedCategory, listSelectedCodeSupplier, rating, fromPrice, toPrice, listSelectedAuthor);
            if (resultList == null || resultList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Không tìm thấy kết quả nào", ""));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Có lỗi xảy ra trong quá trình tìm kiếm", e.getMessage()));
        }
    }

    public void prepareEdit(BookDTO bookDTO) {
        selectedBook = bookDTO;
    }

    public void reverseBookStatusInStock() {
        try {
            if (selectedBook == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chưa chọn sách để cập nhật trạng thái", ""));
                return;
            }
            selectedBook.setBookStatusInStock(1L);
            inventoryService.updateStatusBookInStock(selectedBook, currentStaff);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cập nhật trạng thái sách thành công"));
            searchInventory();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Có lỗi xảy ra trong quá trình cập nhật trạng thái sách", e.getMessage()));
        }
    }

    public void deleteBookInStock() {
        try {
            if (selectedBook == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chưa chọn sách để xóa", ""));
                return;
            }
            selectedBook.setBookStatusInStock(0L);
            inventoryService.updateStatusBookInStock(selectedBook, currentStaff);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Xóa sách thành công"));
            searchInventory();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Có lỗi xảy ra trong quá trình xóa sách", e.getMessage()));
        }
    }
}
