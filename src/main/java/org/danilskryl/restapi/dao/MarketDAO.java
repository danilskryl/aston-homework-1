package org.danilskryl.restapi.dao;

import org.danilskryl.restapi.model.Market;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarketDAO {
    public List<Market> getAllMarkets() {
        String sql = "SELECT * FROM aston.market";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
            throw new RuntimeException(e);
        }
    }

    public Market getMarketById(Long id) {
        String sql = "SELECT * FROM aston.market WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            Market market = new Market();
            if (resultSet.next()) {
                market.setId(resultSet.getLong(1));
                market.setName(resultSet.getString(2));
            }

            return market;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Market saveMarket(Market market) {
        String sql = "INSERT INTO aston.market (name) VALUES (?)";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
            throw new RuntimeException(e);
        }

    }

    public Market updateMarket(Market market) {
        String sql = "UPDATE aston.market SET name = ? WHERE id = ?";

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, market.getName());
            statement.setLong(2, market.getId());

            statement.executeUpdate();
            connection.commit();

            return market;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeMarket(Long id) {
        String sql = "DELETE FROM aston.market WHERE id = ?";

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