package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dto.OrderDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.OrderMapper;
import org.danilskryl.restapi.model.Order;
import org.danilskryl.restapi.repository.OrderRepository;
import org.danilskryl.restapi.repository.impl.OrderRepositoryImpl;
import org.danilskryl.restapi.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDto> mapper;

    public OrderServiceImpl() {
        orderRepository = new OrderRepositoryImpl();
        mapper = new OrderMapper();
    }

    public OrderServiceImpl(OrderRepository orderRepository, Mapper<Order, OrderDto> mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orderList = orderRepository.getAll();
        return orderList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OrderDto getById(Long id) {
        return mapper.toDto(orderRepository.getById(id));
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        Order order = mapper.fromDto(orderDto);
        Order savedOrder = orderRepository.save(order);
        return mapper.toDto(savedOrder);
    }

    @Override
    public OrderDto save(OrderDto orderDto, List<Long> productsId) {
        Order order = mapper.fromDto(orderDto);
        Order savedOrder = orderRepository.save(order, productsId);
        return mapper.toDto(savedOrder);
    }

    @Override
    public OrderDto update(OrderDto orderDto) {
        Order order = mapper.fromDto(orderDto);
        Order updatedOrder = orderRepository.update(order);
        return mapper.toDto(updatedOrder);
    }

    @Override
    public boolean remove(Long id) {
        return orderRepository.remove(id);
    }
}
