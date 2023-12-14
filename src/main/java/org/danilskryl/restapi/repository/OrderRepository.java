package org.danilskryl.restapi.repository;

import org.danilskryl.restapi.model.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order> {
    Order save(Order order, List<Long> productsId);
}
