package org.danilskryl.restapi.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Order;
import org.danilskryl.restapi.repository.OrderRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderRepositoryImpl implements OrderRepository {
    private final ConnectionPool connectionPool;
    private static final String SQL_SELECT_ALL = "SELECT * FROM aston.order_table";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM aston.order_table WHERE id = ?";
    private static final String SQL_INSERT_ORDER = "INSERT INTO aston.order_table (order_date) VALUES (?)";
    private static final String SQL_INSERT_ORDER_PRODUCT = "INSERT INTO aston.orders_products (order_id, product_id) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE aston.order_table SET aston.order_table.order_date = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM aston.order_table WHERE id = ?";

    public OrderRepositoryImpl() {
        connectionPool = new ConnectionPool();
    }

    public OrderRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Order> getAll() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
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
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public Order getById(Long id) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
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
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public Order save(Order order) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement insertOrderStatement = connection.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            insertOrderStatement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            insertOrderStatement.executeUpdate();

            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long orderId = generatedKeys.getLong(1);
                order.setId(orderId);
            }
            connection.commit();

            return order;
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public Order save(Order order, List<Long> productsId) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement insertOrderStatement = connection.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertOrderProductStatement = connection.prepareStatement(SQL_INSERT_ORDER_PRODUCT);
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
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public Order update(Order order) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            statement.setLong(2, order.getId());

            statement.executeUpdate();
            connection.commit();

            return order;
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public boolean remove(Long id) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, id);

            int result = statement.executeUpdate();
            connection.commit();

            return result > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }
}
