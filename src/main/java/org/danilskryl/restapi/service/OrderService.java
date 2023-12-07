package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.OrderDto;

import java.util.List;

public interface OrderService extends BaseService<OrderDto> {
    List<OrderDto> getAll();

    OrderDto getById(Long id);

    OrderDto save(OrderDto orderDto);

    OrderDto save(OrderDto orderDto, List<Long> productsId);

    OrderDto update(OrderDto orderDto);

    boolean remove(Long id);
}
