package org.bpm.abcbook.service;

import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.repository.BooksRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BooksService {
    private static final Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksRepo booksRepo;

    /**
     * Ham lay tat ca sach
     *
     * @return
     */
    public List<Books> findAllBook() throws Exception {
        return booksRepo.findAll();
    }

    /**
     * Ham tim kiem tat ca sach
     *
     * @param title
     * @param fromPrice
     * @param toPrice
     * @param fromRating
     * @param toRating
     * @param publisher
     * @param bookStatus
     * @param bookFormat
     * @param author
     * @param category
     * @param fromAddDate
     * @param toAddDate
     * @return
     */
    public List<Books> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating, String publisher,
                               Long bookStatus, Long bookFormat, String author, String category, Date fromAddDate, Date toAddDate) throws Exception {
        return booksRepo.findAll(title, fromPrice, toPrice, fromRating, toRating, publisher, bookStatus, bookFormat, author, category, fromAddDate, toAddDate);
    }

    public void deleteBook(Long id) throws Exception {
        booksRepo.deleteBook(id);
    }

    /**
     * Cap nhat trang thai sach ve khong hieu luc
     *
     * @param book
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(Books book) throws Exception {
        if (book == null) {
            throw new AppException("UB00001", "book.management.update.book.empty");
        }

        //Kiem tra xem co ton tai hay khong
        if (!booksRepo.existsById(book.getId())) {
            throw new AppException("UB00001", "book.management.update.book.not.exist");
        }

        booksRepo.save(book);
    }

    /**
     * Ham lay sach trong kho
     *
     * @param bookId
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exception
     */
    public List<Books> findBookInStock(Long bookId, Date fromDate, Date toDate) throws Exception {
        return booksRepo.findBookInStock(bookId, fromDate, toDate);
    }

    /**
     * Ham them sach
     *
     * @param book
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertBook(Books book) throws Exception {
        if (book == null) {
            throw new AppException("IB00001", "book.management.insert.book.empty");
        }

        //Kiem tra xem sach da ton tai hay chua
        if (booksRepo.existsByTitle(book.getTitle())) {
            throw new AppException("IB00001", "book.management.insert.book.exist");
        }

        book.setBookStatus(Books.BOOK_STATUS_ACTIVE);
        book.setBookFormat(Books.BOOK_FORMAT_HARDCOVER);
        book.setAddDate(new Date());

        booksRepo.save(book);
    }

    public void deleteListBook(List<Long> idList) throws Exception {
        if (idList == null || idList.isEmpty()) {
            return;
        }
        booksRepo.deleteListBook(idList);
    }
}
