package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.Const;
import org.bpm.abcbook.dto.response.ApiResponse;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.BooksMapper;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Suppliers;
import org.bpm.abcbook.service.BooksService;
import org.bpm.abcbook.service.SuppliersService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.util.*;

@Controller
@Named
@ViewScoped
@Data
public class BooksController implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);
    private List<Books> filteredBooks; // Danh sách sách đã lọc
    private List<Books> booksList;
    private Books selectedBook;
    private Books updateBook;
    private List<Books> selectedBooks;
    private List<String> bookCategory;
    private Long statusFilterValue; // Giá trị bộ lọc trạng thái
    private Long oldStatusFilterValue; // Giá trị bộ lọc trạng thái cũ
    private Books newBook; // Sách mới được thêm vào
    private String publisherUpdate; // Nhà cung cấp được chọn trong danh sách
    private String categoryUpdate;
    private String authorUpdate;
    private String titleUpdate;
    private String descriptionUpdate;
    private String imageUrlUpdate;
    private Long priceUpdate;
    private String currentStaff;

    // Map để ánh xạ category và nhãn hiển thị
    private Map<String, String> categoryLabels;
    private Map<Long, String> statusFilterOptions;

    // Danh sách nhà cung cấp
    private List<Suppliers> suppliersList;

    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private SuppliersService suppliersService;
    @Inject
    private UserSessionBean userSessionBean;

    @PostConstruct
    public void init() {
        try {
            suppliersList = suppliersService.findAllSuppliers();
            getAllBooks();
            statusFilterValue = null;
            newBook = new Books();
            categoryLabels = new HashMap<>();
            statusFilterOptions = new LinkedHashMap<>();

            categoryLabels.put("TIEU_THUYET", "Tiểu thuyết");
            categoryLabels.put("TRUYEN_NGAN", "Truyện ngắn");
            categoryLabels.put("TRUYEN_TRANH", "Truyện tranh");
            categoryLabels.put("TRUYEN_THIEU_NHI", "Truyện thiếu nhi");

            statusFilterOptions.put(1L, "Hiệu lực");
            statusFilterOptions.put(0L, "Không hiệu lực");

            bookCategory = Const.BookCategory.BOOK_CATEGORY;
            currentStaff = userSessionBean == null || userSessionBean.getCurrentStaff() == null ? null : userSessionBean.getCurrentStaff().getStaffCode();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("IB00000", e.getMessage());
        }
    }

    public void prepareAddBook() {
        this.newBook = new Books();
    }

    public void getAllBooks() {
        try {
            booksList = booksService.findAllBook();
            filteredBooks = new ArrayList<>(booksList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("IB00000", e.getMessage());
        }
    }

    public ApiResponse<List<Books>> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating, String publisher,
                                            Long bookStatus, Long bookFormat, String author, String category, Date fromAddDate, Date toAddDate) {
        ApiResponse<List<Books>> apiResponse = ApiResponse.<List<Books>>builder()
                .success(true)
                .build();
        try {
            apiResponse.setData(booksService.findAll(title, fromPrice, toPrice, fromRating, toRating, publisher, bookStatus, bookFormat,
                    author, category, fromAddDate, toAddDate));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("FA0000", e.getMessage());
        }
        return apiResponse;
    }


    public void delete() {
        try {
            booksService.deleteBook(selectedBook.getId());
            getAllBooks();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Xóa thành công"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("FA00000", e.getMessage());
        }
    }

    public void updateBook() {
        try {
            if (selectedBook == null) {
                throw new AppException("UB00001", "book.management.update.book.empty");
            }
            if (updateBook != null) {
                if (updateBook.getTitle() != null && !updateBook.getTitle().isEmpty()) {
                    selectedBook.setTitle(updateBook.getTitle());
                }
                if (updateBook.getAuthor() != null && !updateBook.getAuthor().isEmpty()) {
                    selectedBook.setAuthor(updateBook.getAuthor());
                }
                if (updateBook.getDescription() != null && !updateBook.getDescription().isEmpty()) {
                    selectedBook.setDescription(updateBook.getDescription());
                }
                if (updateBook.getCategory() != null && !updateBook.getCategory().isEmpty()) {
                    selectedBook.setCategory(updateBook.getCategory());
                }
                if (updateBook.getImageUrl() != null && !updateBook.getImageUrl().isEmpty()) {
                    selectedBook.setImageUrl(updateBook.getImageUrl());
                }
                if (updateBook.getPrice() != null) {
                    selectedBook.setPrice(updateBook.getPrice());
                }
                if (updateBook.getPublisher() != null && !updateBook.getPublisher().isEmpty()) {
                    selectedBook.setPublisher(updateBook.getPublisher());
                }
            }
            booksService.updateBook(selectedBook, currentStaff);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cập nhật thành công"));
            getAllBooks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("UB00000", e.getMessage());
        }
    }

    public void reverseBookStatus() {
        try {

            if (Books.BOOK_STATUS_ACTIVE.equals(selectedBook.getBookStatus())) {
                selectedBook.setBookStatus(Books.BOOK_STATUS_INACTIVE);
            } else {
                selectedBook.setBookStatus(Books.BOOK_STATUS_ACTIVE);
            }

            booksService.updateBook(selectedBook, currentStaff);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Kích hoạt thành công"));
            getAllBooks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("UB00000", e.getMessage());
        }
    }

    public void insertBook(Books book) {
        try {
            booksService.insertBook(book);
            getAllBooks();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Đã thêm thành công"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("IB00000", e.getMessage());
        }
    }

    public void prepareEdit(Books bookToEdit) {
        this.selectedBook = bookToEdit;
    }

    public void prepareUpdate(Books bookToEdit) {
        updateBook = new Books();
        selectedBook = bookToEdit;
        publisherUpdate = null;
        categoryUpdate = null;
        authorUpdate = null;
        titleUpdate = null;
        descriptionUpdate = null;
        imageUrlUpdate = null;
        priceUpdate = null;
    }

    public boolean hasSelectedBooks() {
        return this.selectedBooks != null && !this.selectedBooks.isEmpty();
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedBooks()) {
            int size = this.selectedBooks.size();
            return size > 1 ? "Đã chọn " + size : "Đã chọn 1";
        }

        return "Xóa";
    }

    public void deleteSelectedBooks() throws Exception {
        booksService.deleteListBook(this.selectedBooks.stream().map(Books::getId).toList());
        getAllBooks();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Đã xóa"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-books");
        PrimeFaces.current().executeScript("PF('dtBooks').clearFilters()");
    }
}
