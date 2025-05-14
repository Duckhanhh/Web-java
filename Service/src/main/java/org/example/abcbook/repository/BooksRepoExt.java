package org.example.abcbook.repository;

import org.example.abcbook.model.Books;

import java.util.Date;
import java.util.List;

public interface BooksRepoExt {
    List<Books> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating,
                        String publisher, Long bookStatus, Long bookFormat, String author, String category,
                        Date fromAddDate, Date toAddDate) throws Exception;

    void deleteBook(Long id) throws Exception;

    List<Books> findBookInStock(Long bookId, Date fromDate, Date toDate) throws Exception;
}
