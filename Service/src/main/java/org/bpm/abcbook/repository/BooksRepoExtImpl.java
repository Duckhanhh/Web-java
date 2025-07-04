package org.bpm.abcbook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.bpm.abcbook.model.Books;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
            sql.append(" AND title LIKE '%:title%' ");
        }
        if (fromPrice != null) {
            sql.append(" AND price >= :fromPrice ");
        }
        if (toPrice != null) {
            sql.append(" AND price <= :toPrice ");
        }
        if (fromRating > 0) {
            sql.append(" AND price >= :fromRating ");
        }
        if (toRating > 0) {
            sql.append(" AND price <= :toRating ");
        }
        if (publisher != null) {
            sql.append(" AND publisher LIKE '%:publisher%' ");
        }
        if (bookStatus != null) {
            sql.append(" AND status = :bookStatus ");
        }
        if (bookFormat != null) {
            sql.append(" AND format = :bookFormat ");
        }
        if (author != null) {
            sql.append(" AND author LIKE '%:author%' ");
        }
        if (category != null) {
            sql.append(" AND category LIKE '%:category%' ");
        }
        if (fromAddDate != null) {
            sql.append(" AND addDate >= :fromAddDate ");
        }
        if (toAddDate != null) {
            sql.append(" AND addDate < :toAddDate + 1 ");
        }
        Query query = em.createNativeQuery(sql.toString(), Books.class);
        if (title != null) {
            query.setParameter("title", "%:title%");
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
        Query query = em.createNativeQuery("UPDATE Books SET book_status = 0 WHERE book_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Books> findBookInStock(Long bookId, Date fromDate, Date toDate) {
        if (bookId == null) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM Books b INNER JOIN Inventory i ON b.book_id = i.book_id WHERE book_id = :bookId ");
        if (fromDate != null) {
            sql.append(" AND addDate >= :fromAddDate ");
        }
        if (toDate != null) {
            sql.append(" AND addDate < :toAddDate + 1 ");
        }
        Query query = em.createNativeQuery(sql.toString(), Books.class);
        query.setParameter("bookId", bookId);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitle(String title) throws Exception {
        if (title == null) {
            return false;
        }
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM Books WHERE title = :name");
        query.setParameter("name", title);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteListBook(List<Long> idList) throws Exception {
        if (idList == null || idList.isEmpty()) {
            return;
        }
        StringBuilder sql = new StringBuilder("UPDATE Books SET book_status = 0 WHERE book_id IN (");
        for (int i = 0; i < idList.size(); i++) {
            sql.append(":id").append(i);
            if (i < idList.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        Query query = em.createNativeQuery(sql.toString());
        for (int i = 0; i < idList.size(); i++) {
            query.setParameter("id" + i, idList.get(i));
        }
        query.executeUpdate();
    }
}
