package org.bpm.abcbook.service;

import org.bpm.abcbook.model.Books;

import java.util.Date;
import java.util.List;

public interface BooksService {
    /**
     * Ham lay tat ca sach
     *
     * @return
     */
    List<Books> findAllBook() throws Exception;

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
    List<Books> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating, String publisher,
                        Long bookStatus, Long bookFormat, String author, String category, Date fromAddDate, Date toAddDate) throws Exception;

    void deleteBook(Long id) throws Exception;

    void updateBook(Books book) throws Exception;

    List<Books> findBookInStock(Long bookId, Date fromDate, Date toDate) throws Exception;

    void insertBook(Books book) throws Exception;

    void deleteListBook(List<Long> idList) throws Exception;

    List<Books> getAllNameAndCode() throws Exception;

    List<String> getAllAuthor() throws Exception;
}
