package org.bpm.abcbook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.bpm.abcbook.model.Books;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class BooksRepoExtImpl implements BooksRepoExt{
    @PersistenceContext()
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Books> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating, String publisher,
                               Long bookStatus, Long bookFormat, String author, String category, Date fromAddDate, Date toAddDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Books WHERE 1=1 ");
        if (title != null) {
            sql.append(" AND title LIKE '%#title%' ");
        }
        if (fromPrice != null) {
            sql.append(" AND price >= #fromPrice ");
        }
        if (toPrice != null) {
            sql.append(" AND price <= #toPrice ");
        }
        if (fromRating > 0) {
            sql.append(" AND price >= #fromRating ");
        }
        if (toRating > 0) {
            sql.append(" AND price <= #toRating ");
        }
        if (publisher != null) {
            sql.append(" AND publisher LIKE '%#publisher%' ");
        }
        if (bookStatus != null) {
            sql.append(" AND status = #bookStatus ");
        }
        if (bookFormat != null) {
            sql.append(" AND format = #bookFormat ");
        }
        if (author != null) {
            sql.append(" AND author LIKE '%#author%' ");
        }
        if (category != null) {
            sql.append(" AND category LIKE '%#category%' ");
        }
        if (fromAddDate != null) {
            sql.append(" AND addDate >= #fromAddDate ");
        }
        if (toAddDate != null) {
            sql.append(" AND addDate < #toAddDate + 1 ");
        }
        Query query = em.createNativeQuery(sql.toString(), Books.class);
        if (title != null) {
            query.setParameter("title", "%#title%");
        }
        if (fromPrice != null) {
            query.setParameter("fromPrice", fromPrice);
        }
        if (toPrice != null) {
            query.setParameter("toPrice", toPrice);
        }
        if (fromRating > 0) {
            query.setParameter("fromRating", fromRating);
        }
        if (toRating > 0) {
            query.setParameter("toRating", toRating);
        }
        if (publisher != null) {
            query.setParameter("publisher", publisher);
        }
        if (bookStatus != null) {
            query.setParameter("bookStatus", bookStatus);
        }
        if (bookFormat != null) {
            query.setParameter("bookFormat", bookFormat);
        }
        if (author != null) {
            query.setParameter("author", author);
        }
        if (category != null) {
            query.setParameter("category", category);
        }
        if (fromAddDate != null) {
            query.setParameter("fromAddDate", fromAddDate);
        }
        if (toAddDate != null) {
            query.setParameter("toAddDate", toAddDate);
        }
        return query.getResultList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long id) throws Exception {
        if (id == null) {
            return;
        }
        Query query = em.createNativeQuery("UPDATE Books SET book_status = 0 WHERE id = #id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
