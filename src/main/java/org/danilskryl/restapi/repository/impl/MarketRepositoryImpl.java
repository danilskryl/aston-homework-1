package org.danilskryl.restapi.repository.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Market;
import org.danilskryl.restapi.repository.MarketRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MarketRepositoryImpl implements MarketRepository {
    private final ConnectionPool connectionPool;
    private static final String SQL_SELECT_ALL = "SELECT * FROM market";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM market WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO market (name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE market SET name = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM market WHERE id = ?";

    public MarketRepositoryImpl() {
        connectionPool = new ConnectionPool();
    }

    public MarketRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @SneakyThrows
    @Override
    public List<Market> getAll() {
        try (Connection connection = connectionPool.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
                List<Market> result = new ArrayList<>();

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Market market = new Market();
                    market.setId(resultSet.getLong(1));
                    market.setName(resultSet.getString(2));

                    result.add(market);
                }

                return result;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public Market getById(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                Market market = new Market();
                if (resultSet.next()) {
                    market.setId(resultSet.getLong(1));
                    market.setName(resultSet.getString(2));
                }

                return market;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
                throw e;
            }
        }
    }

    @SneakyThrows
    @Override
    public Market save(Market market) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, market.getName());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        market.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Failed to retrieve generated key.");
                    }

                    connection.commit();

                    return market;
                } catch (SQLException e) {
                    log.error(e.getMessage());
                    connection.rollback();
                    throw e;
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public Market update(Market market) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                statement.setString(1, market.getName());
                statement.setLong(2, market.getId());

                statement.executeUpdate();
                connection.commit();

                return market;
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