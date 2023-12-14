package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.OrderDto;

import java.util.List;

public interface OrderService extends BaseService<OrderDto> {
    OrderDto save(OrderDto orderDto, List<Long> productsId);
}
