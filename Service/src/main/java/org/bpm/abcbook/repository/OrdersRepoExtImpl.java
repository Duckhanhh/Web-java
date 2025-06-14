package org.bpm.abcbook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.bpm.abcbook.dto.NumberOrderDTO;
import org.bpm.abcbook.dto.OrderDTO;
import org.bpm.abcbook.dto.RevenueDTO;
import org.bpm.abcbook.util.DataUtil;
import org.bpm.abcbook.util.DbUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Repository
public class OrdersRepoExtImpl implements OrdersRepoExt {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<OrderDTO> findOrder(Long orderId, Long payMethod, Long payStatus, List<Long> listUserId, List<Long> listStatus,
                                    List<String> listShippingCarrier, List<String> listStaff, List<Long> listState, Date fromDate, Date toDate) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT o.order_id, o.status, o.state, o.total_amount, o.payment_method, o.pay_status," +
                " o.shipping_carrier, o.shipping_address, o.user_id, u.first_name, o.data, o.discount_amount, o.order_date, o.shipping_fee, o.staff_confirm, s.first_name " +
                " FROM Orders o " +
                " INNER JOIN Users u ON o.user_id = u.user_id " +
                " LEFT JOIN Staff s ON o.staff_confirm = s.staff_code WHERE 1=1 ");
        if (orderId != null) {
            sql.append(" AND o.order_id = :orderId ");
        }
        if (payMethod != null) {
            sql.append(" AND o.payment_method = :payMethod ");
        }
        if (payStatus != null) {
            sql.append(" AND o.pay_status = :payStatus ");
        }
        if (listUserId != null && !listUserId.isEmpty()) {
            sql.append(" AND o.user_id ");
            sql.append(DbUtil.createInQuery("listUserId", listUserId));
        }
        if (listStatus != null && !listStatus.isEmpty()) {
            sql.append(" AND o.status ");
            sql.append(DbUtil.createInQuery("listStatus", listStatus));
        }
        if (listShippingCarrier != null && !listShippingCarrier.isEmpty()) {
            sql.append(" AND o.shipping_carrier ");
            sql.append(DbUtil.createInQuery("listShippingCarrier", listShippingCarrier));
        }
        if (listStaff != null && !listStaff.isEmpty()) {
            sql.append(" AND o.staff_confirm ");
            sql.append(DbUtil.createInQuery("listStaff", listStaff));
        }
        if (listState != null && !listState.isEmpty()) {
            sql.append(" AND o.state ");
            sql.append(DbUtil.createInQuery("listState", listState));
        }
        if (fromDate != null) {
            sql.append(" AND DATE(o.order_date) >= DATE(:fromDate) ");
        }
        if (toDate != null) {
            sql.append(" AND DATE(o.order_date) <= DATE(:toDate) ");
        }
        sql.append(" LIMIT 1000 ");
        Query query = em.createNativeQuery(sql.toString());
        if (orderId != null) {
            query.setParameter("orderId", orderId);
        }
        if (payMethod != null) {
            query.setParameter("payMethod", payMethod);
        }
        if (payStatus != null) {
            query.setParameter("payStatus", payStatus);
        }
        if (listUserId != null && !listUserId.isEmpty()) {
            DbUtil.setParamInQuery(query, "listUserId", listUserId);
        }
        if (listStatus != null && !listStatus.isEmpty()) {
            DbUtil.setParamInQuery(query, "listStatus", listStatus);
        }
        if (listShippingCarrier != null && !listShippingCarrier.isEmpty()) {
            DbUtil.setParamInQuery(query, "listShippingCarrier", listShippingCarrier);
        }
        if (listStaff != null && !listStaff.isEmpty()) {
            DbUtil.setParamInQuery(query, "listStaff", listStaff);
        }
        if (listState != null && !listState.isEmpty()) {
            DbUtil.setParamInQuery(query, "listState", listState);
        }
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }

        List<Object[]> result = query.getResultList();
        if (result == null || result.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderDTO> listOrder = new ArrayList<>();

        for (Object[] row : result) {
            int i = 0;
            OrderDTO dto = new OrderDTO();

            dto.setOrderId(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setStatus(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setState(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setTotalAmount(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setPaymentMethod(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setPayStatus(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setShippingCarrier(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setShippingAddress(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setUserId(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setUserFirstName(row[i] != null ? row[i].toString() : null);
            i++;
            if (row[i] != null) {
                dto.getNecessaryData(row[i].toString());
            }
            i++;
            dto.setDiscountAmount(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setOrderDate(row[i] != null ? (Date) row[i] : null);
            i++;
            dto.setShippingFee(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            dto.setStaffConfirm(row[i] != null ? row[i].toString() : null);
            i++;
            dto.setStaffFirstName(row[i] != null ? row[i].toString() : null);

            listOrder.add(dto);
        }
        return listOrder;
    }

    @Override
    public List<RevenueDTO> getRevenue() throws Exception {
        Query query = em.createNativeQuery("SELECT SUM(total_amount), DATE_FORMAT(order_date, '%m/%Y') FROM Orders WHERE status = 3 " +
                "GROUP BY DATE_FORMAT(order_date, '%m/%Y') ORDER BY DATE_FORMAT(order_date, '%m/%Y') asc LIMIT 1000 ");
        List<Object[]> result = query.getResultList();
        if (result == null || result.isEmpty()) {
            return new ArrayList<>();
        }

        List<RevenueDTO> listRevenue = new LinkedList<>();
        for (Object[] row : result) {
            int i = 0;
            RevenueDTO revenueDTO = new RevenueDTO();

            revenueDTO.setAmount(row[i] != null ? ((Number) row[i]).longValue() : null);
            i++;
            revenueDTO.setMonth(row[i] != null ? row[i].toString() : null);

            listRevenue.add(revenueDTO);
        }
        return listRevenue;
    }

    @Override
    public List<NumberOrderDTO> getNumberOrder() throws Exception {
        Query query = em.createNativeQuery("SELECT DATE_FORMAT(order_date, '%m/%Y') month, COUNT(*), SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END)" +
                "FROM Orders WHERE YEAR(order_date) = YEAR(CURDATE()) GROUP BY DATE_FORMAT(order_date, '%m/%Y') ORDER BY month");
        List<Object[]> resultList = query.getResultList();
        if (DataUtil.isNullOrEmpty(resultList)) {
            return new ArrayList<>();
        }

        List<NumberOrderDTO> numberOrderDTOList = new LinkedList<>();
        for (Object[] row : resultList) {
            int i = 0;
            NumberOrderDTO dto = new NumberOrderDTO();
            dto.setMonth(row[i] != null ? row[i].toString() : null); i++;
            dto.setTotalQuantity(row[i] != null ? ((Number) row[i]).longValue() : null); i++;
            dto.setNumberCompletedOrders(row[i] != null ? ((Number) row[i]).longValue() : null);
            numberOrderDTOList.add(dto);
        }

        return numberOrderDTOList;
    }
}
