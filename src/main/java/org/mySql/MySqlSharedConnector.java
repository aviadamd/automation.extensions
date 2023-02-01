package org.mySql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.ibatis.jdbc.SQL;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySqlSharedConnector {

    private SQL sqlQuery;
    private Connection connection;

    public MySqlSharedConnector setQuery(SQL sqlQuery) {
        this.sqlQuery = sqlQuery;
        return this;
    }

    public MySqlSharedConnector(String connection, String user, String pass) {
        try {
            if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
                this.connection = DriverManager.getConnection(connection);
            } else this.connection = DriverManager.getConnection(connection, user, pass);
        } catch (Exception exception) {
            Assertions.fail(exception.getMessage(), exception);
        }
    }
    public <T> List<T> queryToObjectsList(Class<T> tClass) {
        try {
            BeanListHandler<T> beanListHandler = new BeanListHandler<>(tClass);
            QueryRunner queryRunner = new QueryRunner();
            return queryRunner.query(this.connection, this.sqlQuery.toString(), beanListHandler);
        } catch (Exception exception) {
            Assertions.fail("queryToObjectsList error ", exception);
            return new ArrayList<>();
        }
    }

    public void closeConnection() {
        DbUtils.closeQuietly(this.connection);
    }
}
