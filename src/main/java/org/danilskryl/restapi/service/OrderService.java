package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.OrderTo;

import java.util.List;

public interface OrderService {
    List<OrderTo> getAllOrders();

    OrderTo getOrderById(Long id);

    OrderTo saveOrder(OrderTo orderTo, List<Long> productsId);

    OrderTo updateOrder(OrderTo orderTo);

    boolean removeOrder(Long id);
}
