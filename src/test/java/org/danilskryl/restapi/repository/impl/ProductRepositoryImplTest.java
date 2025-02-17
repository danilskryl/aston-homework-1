package org.danilskryl.restapi.repository.impl;

import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    @Mock
    private ConnectionPool connectionPool;
    private ProductRepositoryImpl productRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private ProductRepositoryImpl spyRep;
    private Product productExpected;
    private static final String SQL_INSERT = "INSERT INTO product(name, description, market_id) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM product WHERE id = ?";

    @BeforeEach
    void setup() {
        productRepository = new ProductRepositoryImpl(connectionPool);
        spyRep = spy(productRepository);

        productExpected = new Product();
        productExpected.setId(1L);
        productExpected.setName("Test product");
        productExpected.setDescription("Test description");
        productExpected.setMarketId(1L);
    }

    @Test
    void createProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        productRepository.save(productExpected);

        verify(connection).prepareStatement(SQL_INSERT, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setString(1, productExpected.getName());
        verify(preparedStatement).setString(2, productExpected.getDescription());
        verify(preparedStatement).setLong(3, productExpected.getMarketId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void getProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyRep.getById(1L)).thenReturn(productExpected);

        Product productActual = spyRep.getById(1L);

        assertNotNull(productActual);
        assertEquals(productExpected, productActual);
        verify(connection).prepareStatement(SQL_SELECT_BY_ID);
    }

    @Test
    void updateProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Product updateProduct = new Product();
        updateProduct.setId(1L);
        updateProduct.setName("Updated test product");
        updateProduct.setDescription("Updated test product description");
        updateProduct.setMarketId(1L);

        Product product = productRepository.update(updateProduct);

        assertNotNull(product);
        assertEquals(updateProduct, product);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setString(1, updateProduct.getName());
        verify(preparedStatement).setString(2, updateProduct.getDescription());
        verify(preparedStatement).setLong(3, updateProduct.getMarketId());
    }
}