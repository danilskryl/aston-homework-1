package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dao.OrderDAO;
import org.danilskryl.restapi.dto.OrderTo;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.OrderMapper;
import org.danilskryl.restapi.model.Order;
import org.danilskryl.restapi.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;
    private final Mapper<Order, OrderTo> mapper = new OrderMapper();

    public OrderServiceImpl() {
        orderDAO = new OrderDAO();
    }

    @Override
    public List<OrderTo> getAllOrders() {
        return orderDAO.getAllOrders().stream().map(mapper::toDto).toList();
    }

    @Override
    public OrderTo getOrderById(Long id) {
        return mapper.toDto(orderDAO.getOrderById(id));
    }

    @Override
    public OrderTo saveOrder(OrderTo orderTo, List<Long> productsId) {
        Order order = mapper.toEntity(orderTo);
        return mapper.toDto(orderDAO.saveOrder(order, productsId));
    }

    @Override
    public OrderTo updateOrder(OrderTo orderTo) {
        Order order = mapper.toEntity(orderTo);
        return mapper.toDto(orderDAO.updateOrder(order));
    }

    @Override
    public boolean removeOrder(Long id) {
        return orderDAO.deleteOrder(id);
    }
}
