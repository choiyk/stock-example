package com.example.stock.facade;

import com.example.stock.Service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    @Resource(name = "namedLockDatasource")
    private final DataSource datasource;

    private final StockService stockService;

    public void decrease(long id, long quantity) {
        String lockName = String.valueOf(id);

        try(Connection connection = datasource.getConnection()) {
            try {
                getLock(connection, lockName, 3000);

                stockService.decrease(id, quantity);
            }
            finally {
                releaseLock(connection, lockName);
            }
        }
        catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void getLock(Connection connection, String lockName, int timeoutSeconds) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {
            preparedStatement.setString(1, lockName);
            preparedStatement.setInt(2, timeoutSeconds);

            executeQuery(preparedStatement);
        }
    }

    private void releaseLock(Connection connection, String lockName) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)) {
            preparedStatement.setString(1, lockName);

            executeQuery(preparedStatement);
        }
    }

    private void executeQuery(PreparedStatement preparedStatement) throws SQLException {
        try(ResultSet resultSet = preparedStatement.executeQuery()) {
            if(!resultSet.next())
                throw new RuntimeException("Named Lock 쿼리 결과 값이 없습니다.");

            int result = resultSet.getInt(1);

            if(result != 1)
                throw new RuntimeException("Named Lock 쿼리 결과 값이 1이 아닙니다.");
        }
    }

}
