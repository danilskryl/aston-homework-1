package org.danilskryl.restapi.repository.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Product;
import org.danilskryl.restapi.repository.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository {
    private final ConnectionPool connectionPool;
    private static final String SQL_SELECT_ALL = "SELECT * FROM product";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM product WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO product(name, description, market_id) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE product SET name = ?, description = ?, market_id = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM product WHERE id = ?";

    public ProductRepositoryImpl() {
        connectionPool = new ConnectionPool();
    }

    public ProductRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @SneakyThrows
    @Override
    public List<Product> getAll() {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
                List<Product> products = new ArrayList<>();

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getLong(1));
                    product.setName(resultSet.getString(2));
                    product.setDescription(resultSet.getString(3));
                    product.setMarketId(resultSet.getLong(4));

                    products.add(product);
                }
                connection.commit();

                return products;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public Product getById(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                Product product = new Product();
                if (resultSet.next()) {
                    product.setId(resultSet.getLong(1));
                    product.setName(resultSet.getString(2));
                    product.setDescription(resultSet.getString(3));
                    product.setMarketId(resultSet.getLong(4));
                }
                connection.commit();

                return product;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public Product save(Product product) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getDescription());
                statement.setLong(3, product.getMarketId());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Failed to retrieve generated key.");
                    }
                }
                connection.commit();

                return product;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public Product update(Product product) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getDescription());
                statement.setLong(3, product.getMarketId());
                statement.setLong(4, product.getId());

                statement.executeUpdate();
                connection.commit();

                return product;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public boolean remove(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, id);

                int result = statement.executeUpdate();
                connection.commit();

                return result > 0;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }
}