package org.danilskryl.restapi.dao;

import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> getAll() {
        String sql = "SELECT * FROM aston.order_table";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Order> orders = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong(1));
                order.setOrderDate(resultSet.getTimestamp(2).toLocalDateTime());

                orders.add(order);
            }
            connection.commit();

            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getById(Long id) {
        String sql = "SELECT * FROM aston.order_table WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Order order = new Order();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
                order.setOrderDate(resultSet.getTimestamp(2).toLocalDateTime());
            }
            connection.commit();

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order save(Order order, List<Long> productsId) {
        String insertOrderSql = "INSERT INTO aston.order_table (order_date) VALUES (?)";
        String insertOrderProductSql = "INSERT INTO aston.orders_products (order_id, product_id) VALUES (?, ?)";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertOrderProductStatement = connection.prepareStatement(insertOrderProductSql)) {
            insertOrderStatement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            insertOrderStatement.executeUpdate();

            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long orderId = generatedKeys.getLong(1);
                order.setId(orderId);

                for (Long productId : productsId) {
                    insertOrderProductStatement.setLong(1, orderId);
                    insertOrderProductStatement.setLong(2, productId);
                    insertOrderProductStatement.executeUpdate();
                }
            }
            connection.commit();

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order update(Order order) {
        String sql = "UPDATE aston.order_table SET aston.order_table.order_date = ? WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            statement.setLong(2, order.getId());

            statement.executeUpdate();
            connection.commit();

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(Long id) {
        String sql = "DELETE FROM aston.order_table WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            int result = statement.executeUpdate();
            connection.commit();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
