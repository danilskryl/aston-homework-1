package org.danilskryl.restapi.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.danilskryl.restapi.config.ConnectionPool;
import org.danilskryl.restapi.model.Market;
import org.danilskryl.restapi.repository.MarketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MarketRepositoryImpl implements MarketRepository {
    private final ConnectionPool connectionPool;
    private static final String SQL_SELECT_ALL = "SELECT * FROM aston.market";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM aston.market WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO aston.market (name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE aston.market SET name = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM aston.market WHERE id = ?";

    public MarketRepositoryImpl() {
        connectionPool = new ConnectionPool();
    }

    public MarketRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Market> getAll() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);

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
    public Market getById(Long id) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);

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
    public Market save(Market market) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, market.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    market.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Failed to retrieve generated key.");
                }
            }
            connection.commit();

            return market;
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
    public Market update(Market market) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, market.getName());
            statement.setLong(2, market.getId());

            statement.executeUpdate();
            connection.commit();

            return market;
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