package org.utils.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.ibatis.jdbc.SQL;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySqlSharedConnector {

    private SQL sqlQuery;
    private final Connection connection;

    public MySqlSharedConnector setQuery(SQL sqlQuery) {
        this.sqlQuery = sqlQuery;
        return this;
    }

    public MySqlSharedConnector(String connection, String user, String pass) {
        this.connection = this.setConnection(connection, user, pass);
    }

    public synchronized <T> List<T> findsBy(Class<T> tClass) {
        try {
            BeanListHandler<T> beanListHandler = new BeanListHandler<>(tClass);
            QueryRunner queryRunner = new QueryRunner();
            log.info("create query with: " + this.sqlQuery.toString());
            log.info("with bean: " + tClass.toString());
            return queryRunner.query(this.connection, this.sqlQuery.toString(), beanListHandler);
        } catch (Exception exception) {
            Assertions.fail("queryToObjectsList error ", exception);
            return new ArrayList<>();
        }
    }

    public synchronized void closeConnection() {
        try {
            DbUtils.closeQuietly(this.connection);
        } catch (Exception exception) {
            log.debug("close connection" +exception.getMessage());
        }
    }

    public synchronized DatabaseMetaData getMetaData() {
        try {
            return this.connection.getMetaData();
        } catch (SQLException sqlException) {
            throw new RuntimeException("sql get meta data error ", sqlException);
        }
    }

    private synchronized Connection setConnection(String connection, String user, String pass) {
        try {
            return user.isEmpty() || pass.isEmpty()
                    ? DriverManager.getConnection(connection)
                    : DriverManager.getConnection(connection, user, pass);
        } catch (SQLException sqlException) {
            throw new RuntimeException("set sql connection error ", sqlException);
        }
    }

}

