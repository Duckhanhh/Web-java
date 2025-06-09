package org.bpm.abcbook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.util.DbUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class InventoryRepoExtImpl implements InventoryRepoExt {
    @PersistenceContext()
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAllInStock(List<String> insertUser, Date fromDate, Date toDate, Long statusInStock, List<Long> listBookId,
                                        Long bookFormat, List<String> listCategory, List<String> listSupplierCode, Integer rating,
                                        Long fromPrice, Long toPrice, List<String> listAuthor) {
        StringBuilder sql = new StringBuilder("SELECT b.book_id, b.title, b.author, b.category, b.supplier_code, b.price, b.rating_average, i.status, b.book_format, b.description," +
                " b.image_url, i.insert_date, b.book_code, i.insert_user, i.quantity" +
                " FROM Inventory i INNER JOIN Books b ON i.book_id = b.book_id " +
                " INNER JOIN Suppliers s ON b.supplier_code = s.supplier_code WHERE 1=1 ");

        if (insertUser != null && !insertUser.isEmpty()) {
            sql.append(" AND i.insert_user ");
            sql.append(DbUtil.createInQuery("insertUser", insertUser));
        }
        if (fromDate != null) {
            sql.append(" AND DATE(i.insert_date) >= DATE(:fromDate) ");
        }
        if (toDate != null) {
            sql.append(" AND DATE(i.insert_date) <= DATE(:toDate) ");
        }
        if (statusInStock != null) {
            sql.append(" AND i.status = :statusInStock ");
        }
        if (listBookId != null && !listBookId.isEmpty()) {
            sql.append(" AND i.book_id ");
            sql.append(DbUtil.createInQuery("listBookId", listBookId));
        }
        if (bookFormat != null) {
            sql.append(" AND b.book_format = :bookFormat ");
        }
        if (listCategory != null && !listCategory.isEmpty()) {
            sql.append(" AND b.category ");
            sql.append(DbUtil.createInQuery("listCategory", listCategory));
        }
        if (listSupplierCode != null && !listSupplierCode.isEmpty()) {
            sql.append(" AND s.supplier_code ");
            sql.append(DbUtil.createInQuery("listSupplierCode", listSupplierCode));
        }
        if (rating != null && rating > 0) {
            sql.append(" AND b.rating_average = :rating ");
        }
        if (fromPrice != null) {
            sql.append(" AND b.price >= :fromPrice ");
        }
        if (toPrice != null) {
            sql.append(" AND b.price <= :toPrice ");
        }
        if (listAuthor != null && !listAuthor.isEmpty()) {
            sql.append(" AND b.author ");
            sql.append(DbUtil.createInQuery("listAuthor", listAuthor));
        }

        Query query = em.createNativeQuery(sql.toString());

        if (insertUser != null && !insertUser.isEmpty()) {
            DbUtil.setParamInQuery(query, "insertUser", insertUser);
        }
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (statusInStock != null) {
            query.setParameter("statusInStock", statusInStock);
        }
        if (listBookId != null && !listBookId.isEmpty()) {
            DbUtil.setParamInQuery(query, "listBookId", listBookId);
        }
        if (bookFormat != null) {
            query.setParameter("bookFormat", bookFormat);
        }
        if (listCategory != null && !listCategory.isEmpty()) {
            DbUtil.setParamInQuery(query, "listCategory", listCategory);
        }
        if (listSupplierCode != null && !listSupplierCode.isEmpty()) {
            DbUtil.setParamInQuery(query, "listSupplierCode", listSupplierCode);
        }
        if (rating != null && rating > 0) {
            query.setParameter("rating", rating);
        }
        if (fromPrice != null) {
            query.setParameter("fromPrice", fromPrice);
        }
        if (toPrice != null) {
            query.setParameter("toPrice", toPrice);
        }
        if (listAuthor != null && !listAuthor.isEmpty()) {
            DbUtil.setParamInQuery(query, "listAuthor", listAuthor);
        }

        List<Object[]> results = query.getResultList();
        List<BookDTO> listBook = new ArrayList<>();
        for (Object[] row : results) {
            int i = 0;
            BookDTO dto = new BookDTO();
            dto.setId(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setTitle(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setAuthor(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setCategory(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setPublisher(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setPrice(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setRating(row[i] != null ? ((Number) row[i]).doubleValue() : 0.0);
            i++;
            dto.setBookStatusInStock(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setBookFormat(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setDescription(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setImageUrl(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setInsertDate(row[i] != null ? (Date) row[i] : null);
            i++;
            dto.setBookCode(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setInsertUser(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setQuantity(row[i] != null ? ((Number) row[i]).intValue() : 0);
            listBook.add(dto);
        }

        return listBook;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory findByBookId(Long bookId) throws Exception {
        if (bookId == null) {
            return null;
        }
        Query query = em.createQuery("SELECT i FROM Inventory i WHERE i.bookId = :bookId", Inventory.class);
        query.setParameter("bookId", bookId);
        return (Inventory) query.getResultList().get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> findAllByListBookId(List<Long> listBookId) throws Exception {
        if (listBookId == null || listBookId.isEmpty()) {
            return new ArrayList<>();
        }
        Query query = em.createQuery("SELECT i FROM Inventory i WHERE i.bookId " + DbUtil.createInQuery("listBookId", listBookId), Inventory.class);
        DbUtil.setParamInQuery(query, "listBookId", listBookId);
        return query.getResultList();
    }
}
