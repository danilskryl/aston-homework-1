package org.danilskryl.restapi.mapper;

import org.danilskryl.restapi.dto.OrderTo;
import org.danilskryl.restapi.model.Order;

public class OrderMapper implements Mapper<Order, OrderTo> {
    @Override
    public Order toEntity(OrderTo dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderDate(dto.getOrderDate());

        return order;
    }

    @Override
    public OrderTo toDto(Order market) {
        return OrderTo.builder()
                .id(market.getId())
                .orderDate(market.getOrderDate())
                .build();
    }
}
