package org.danilskryl.restapi.dao;

import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAll() {
        String sql = "SELECT * FROM aston.product";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
            throw new RuntimeException(e);
        }
    }

    public Product getById(Long id) {
        String sql = "SELECT * FROM aston.product WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
            throw new RuntimeException(e);
        }
    }

    public Product save(Product product) {
        String sql = "INSERT INTO aston.product(name, description, market_id) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
            throw new RuntimeException(e);
        }
    }

    public Product update(Product product) {
        String sql = "UPDATE aston.product SET name = ?, description = ?, market_id = ? WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setLong(3, product.getMarketId());
            statement.setLong(4, product.getId());

            statement.executeUpdate();
            connection.commit();

            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(Long id) {
        String sql = "DELETE FROM aston.product WHERE id = ?";

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
