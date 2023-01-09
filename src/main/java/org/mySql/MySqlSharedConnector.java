package org.mySql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.ibatis.jdbc.SQL;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.Description;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class MySqlSharedConnector {
    private Connection connection;
    public Connection getConnection() {
        return connection;
    }
    private SharedQuery sharedQuery;
    public SharedQuery build() { return this.sharedQuery; }

    public MySqlSharedConnector(String connection) {
        try {
            this.connection = DriverManager.getConnection(connection);
            this.sharedQuery = new SharedQuery(this.connection);
        } catch (Exception exception) {
            Assertions.fail(exception.getMessage(), exception);
        }
    }
    public MySqlSharedConnector(String connection, String user, String pass) {
        try {
            this.connection = DriverManager.getConnection(connection, user, pass);
            this.sharedQuery = new SharedQuery(this.connection);
        } catch (Exception exception) {
            Assertions.fail(exception.getMessage(), exception);
        }
    }

    @Description("step ")
    public MySqlSharedConnector step(Consumer<SharedQuery> sharedQuery) {
        try {
            sharedQuery.accept(this.sharedQuery);
        } catch (Exception exception) {
            Assertions.fail(exception.getMessage(), exception);
        }
        return this;
    }
    public static class SharedQuery {
        private SQL sqlQuery;
        private final Connection connection;

        public Connection getConnection() { return this.connection; }
        public SharedQuery(Connection connection) { this.connection = connection; }
        public void setQuery(SQL sqlQuery) { this.sqlQuery = sqlQuery; }

        public Optional<ResultSet> resultSet() {
            Optional<ResultSet> resultSet = Optional.empty();
            try {
                return Optional.ofNullable(this.connection.createStatement().executeQuery(this.sqlQuery.toString()));
            } catch (Exception exception) {
                Assertions.fail(exception.getMessage(), exception);
            }
            return resultSet;
        }

        public <T> List<T> queryToObjectsList(Class<T> tClass) {
            try {
                BeanListHandler<T> beanListHandler = new BeanListHandler<>(tClass);
                QueryRunner queryRunner = new QueryRunner();
                return queryRunner.query(this.connection, this.sqlQuery.toString(), beanListHandler);
            } catch (Exception exception) {
                Assertions.fail(exception.getMessage(), exception);
                return new ArrayList<>();
            }
        }

        public void closeConnection() { DbUtils.closeQuietly(this.connection); }
    }
}
