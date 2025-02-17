package org.danilskryl.restapi.repository.impl;

import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Market;
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
class MarketRepositoryImplTest {
    @Mock
    private ConnectionPool connectionPool;
    private MarketRepositoryImpl marketRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private MarketRepositoryImpl spyRep;
    private Market marketExpected;
    private static final String SQL_INSERT = "INSERT INTO market (name) VALUES (?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM market WHERE id = ?";

    @BeforeEach
    void setup() {
        marketRepository = new MarketRepositoryImpl(connectionPool);
        spyRep = spy(marketRepository);

        marketExpected = new Market();
        marketExpected.setId(1L);
        marketExpected.setName("Test market");
    }

    @Test
    void createProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        marketRepository.save(marketExpected);

        verify(connection).prepareStatement(SQL_INSERT, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setString(1, marketExpected.getName());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void getProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyRep.getById(1L)).thenReturn(marketExpected);

        Market marketActual = spyRep.getById(1L);

        assertNotNull(marketActual);
        assertEquals(marketExpected, marketActual);
        verify(connection).prepareStatement(SQL_SELECT_BY_ID);
    }

    @Test
    void updateProductTest() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Market updateMarket = new Market();
        updateMarket.setId(1L);
        updateMarket.setName("Updated test product");

        Market market = marketRepository.update(updateMarket);

        assertNotNull(market);
        assertEquals(updateMarket, market);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setString(1, updateMarket.getName());
    }
}