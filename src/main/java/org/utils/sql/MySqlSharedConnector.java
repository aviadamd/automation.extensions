package org.utils.sql;

import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.ibatis.jdbc.SQL;
import org.extensions.report.ExtentTestManager;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class MySqlSharedConnector {
    private final Connection connection;

    /**
     * MySqlSharedConnector constructor
     * @param connection string connection
     * @param user user
     * @param pass pass
     */
    public MySqlSharedConnector(String connection, String user, String pass) {
        this.connection = this.setConnection(connection, user, pass);
    }

    /**
     * findsBy
     * @param tClass pojo class
     * @param <T> the type of pojo class
     * @return generate pojo class
     */
    public synchronized <T> List<T> findsBy(SQL sqlQuery, Class<T> tClass) {
        try {
            BeanListHandler<T> beanListHandler = new BeanListHandler<>(tClass);
            QueryRunner queryRunner = new QueryRunner();
            this.print(Status.INFO,"create query with: " + sqlQuery.toString());
            this.print(Status.INFO,"with bean object : " + tClass.toString());
            return queryRunner.query(this.connection, sqlQuery.toString(), beanListHandler);
        } catch (Exception exception) {
            Assertions.fail("queryToObjectsList error ", exception);
            return new ArrayList<>();
        }
    }

    /**
     * closeConnection
     * optional close db instance
     * @param onFail
     *  INFO("Info", 10),
     *  PASS("Pass", 20),
     *  WARNING("Warning", 30),
     *  SKIP("Skip", 40),
     *  FAIL("Fail", 50);
     */
    public synchronized void closeConnection(Status onFail) {
        try {
            DbUtils.closeQuietly(this.connection);
        } catch (Exception exception) {
            final String errorMessage = "sql close connection error: " + exception.getMessage();
            this.print(onFail, errorMessage);
            if (onFail == Status.FAIL) Assertions.fail(errorMessage, exception);
        }
    }

    /**
     * getMetaData
     * @return DatabaseMetaData instance
     */
    public synchronized DatabaseMetaData getMetaData() {
        try {
            return this.connection.getMetaData();
        } catch (SQLException sqlException) {
            this.print(Status.FAIL,"sql get meta data error " + sqlException.getMessage());
            throw new RuntimeException("sql get meta data error ", sqlException);
        }
    }

    /**
     * setConnection
     * @param connection string connection
     * @param user user
     * @param pass pass
     * @return connection instance
     */
    private synchronized Connection setConnection(String connection, String user, String pass) {
        try {
            if (connection == null || connection.isEmpty()) Assertions.fail("connection forbidden to null or empty");
            return user != null && !user.isEmpty() && pass != null && !pass.isEmpty()
                    ? DriverManager.getConnection(connection, user, pass)
                    : DriverManager.getConnection(connection);
        } catch (SQLException sqlException) {
            this.print(Status.FAIL,"set sql connection error " + sqlException.getMessage());
            throw new RuntimeException("set sql connection error ", sqlException);
        }
    }

    /**
     * print
     * to ExtentTestManager report if instance is not null
     * @param status
     *   INFO("Info", 10),
     *   PASS("Pass", 20),
     *   WARNING("Warning", 30),
     *   SKIP("Skip", 40),
     *   FAIL("Fail", 50);
     * @param message to report
     */
    private synchronized void print(Status status, String message) {
        try {
            ExtentTestManager.getInstance().log(status, message);
        } catch (Exception ignore) {
            log.error(message);
        }
    }
}

