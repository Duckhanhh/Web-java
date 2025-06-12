package org.bpm.abcbook.repository;

import org.bpm.abcbook.dto.OrderDTO;
import org.bpm.abcbook.dto.RevenueDTO;

import java.util.Date;
import java.util.List;

public interface OrdersRepoExt {
    List<OrderDTO> findOrder(Long orderId, Long payMethod, Long payStatus, List<Long> listUserId, List<Long> listStatus,
                             List<String> listShippingCarrier, List<String> listStaff, List<Long> listState,
                             Date fromDate, Date toDate) throws Exception;

    List<RevenueDTO> getRevenue() throws Exception;
}
