package org.danilskryl.restapi.mapper;

import org.danilskryl.restapi.dto.OrderDto;
import org.danilskryl.restapi.model.Order;

public class OrderMapper implements Mapper<Order, OrderDto> {
    @Override
    public Order fromDto(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());

        return order;
    }

    @Override
    public OrderDto toDto(Order market) {
        return OrderDto.builder()
                .id(market.getId())
                .orderDate(market.getOrderDate())
                .build();
    }
}
