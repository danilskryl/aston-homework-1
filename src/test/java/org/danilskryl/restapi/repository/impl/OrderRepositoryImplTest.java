package org.danilskryl.restapi.repository.impl;

import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {
    @Mock
    private ConnectionPool connectionPool;
    private OrderRepositoryImpl orderRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private OrderRepositoryImpl spyRep;
    private Order orderExpected;
    private static final String SQL_INSERT = "INSERT INTO aston.order_table (order_date) VALUES (?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM aston.order_table WHERE id = ?";

    @BeforeEach
    void setup() {
        orderRepository = new OrderRepositoryImpl(connectionPool);
        spyRep = spy(orderRepository);

        orderExpected = new Order();
        orderExpected.setId(1L);
        orderExpected.setOrderDate(LocalDateTime.parse("2021-03-03T12:00:33"));
    }

    @Test
    void createProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        orderRepository.save(orderExpected);

        verify(connection).prepareStatement(SQL_INSERT, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setTimestamp(1, Timestamp.valueOf(orderExpected.getOrderDate()));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void getProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyRep.getById(1L)).thenReturn(orderExpected);

        Order orderActual = spyRep.getById(1L);

        assertNotNull(orderActual);
        assertEquals(orderExpected, orderActual);
        verify(connection).prepareStatement(SQL_SELECT_BY_ID);
    }

    @Test
    void updateProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Order updateOrder = new Order();
        updateOrder.setId(1L);
        updateOrder.setOrderDate(LocalDateTime.parse("2023-05-05T09:33:22"));

        Order market = orderRepository.update(updateOrder);

        assertNotNull(market);
        assertEquals(updateOrder, market);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setTimestamp(1, Timestamp.valueOf(updateOrder.getOrderDate()));
    }
}