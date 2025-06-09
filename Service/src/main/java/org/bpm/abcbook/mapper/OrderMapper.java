package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.OrderDTO;
import org.bpm.abcbook.model.Orders;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Orders OrderDTOToOrders(OrderDTO orderDTO);
}
