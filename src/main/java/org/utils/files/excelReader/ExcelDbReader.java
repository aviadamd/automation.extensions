package org.utils.files.excelReader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ExcelDbReader {
    public static class UpdateQueryBuilder {
        private Connection connection;
        private Fillo reader;
        private String fileLocation;
        private int result = 0;
        public Optional<Connection> getConnection() {
            try {
                if (this.connection == null)
                    this.connection = this.reader.getConnection(this.fileLocation);
                return Optional.ofNullable(this.connection);
            } catch (Exception exception) {
                printError(exception,"\"get connection error\"");
                return Optional.empty();
            }
        }
        public UpdateQueryBuilder setConnection(String fileLocation) {
            this.fileLocation = fileLocation;
            this.reader = new Fillo();
            return this;
        }
        public UpdateQueryBuilder update(String query) {
            if (this.getConnection().isPresent()) {
                try {
                    this.result = this.getConnection().get().executeUpdate(query);
                } catch (Exception exception) {
                    printError(exception,"update");
                }
            }
            return this;
        }

        public int build() { return this.result; }
    }

    public static class QueryBuilder {
        private Connection connection;

        private java.sql.Connection sqlConnection;
        private Fillo reader;
        private String fileLocation;
        private Recordset recordset;
        private Optional<Connection> getConnection() {
            try {
                if (this.connection == null)
                    this.connection = this.reader.getConnection(this.fileLocation);
                return Optional.ofNullable(this.connection);
            } catch (Exception exception) {
                printError(exception,"\"get connection error\"");
                return Optional.empty();
            }
        }
        public QueryBuilder setConnection(String fileLocation) {
            this.fileLocation = fileLocation;
            this.reader = new Fillo();
            return this;
        }
        public QueryBuilder query(String query) {
            try {
                if (this.getConnection().isPresent()) {
                    recordset = this.getConnection().get().executeQuery(query);
                }
            } catch (Exception exception) {
                printError(exception,"\"query error\"");
            }
            return this;
        }
        public QueryBuilder where(String condition) {
            try {
                if (this.getConnection().isPresent()) {
                    recordset.where(condition);
                }
            } catch (Exception exception) {
                printError(exception,"\"query where error\"");
            }
            return this;
        }

        public Recordset build() { return recordset; }
        public void close() {
            if (this.recordset != null) this.recordset.close();
        }
    }

    public static <T> MultiValuedMap<Integer, List<String>> queryResultMap(Recordset recordset, Class<T> objectClass) {
        MultiValuedMap<Integer, List<String>> setObjectMap = new ArrayListValuedHashMap<>();
        try {

            int i = 1;
            Field[] fieldsList = objectClass.getDeclaredFields();
            List<String> collector = new ArrayList<>();

            while (recordset.next()) {

                collector.addAll(Stream.of(fieldsList)
                        .map(field -> {
                            try { return recordset.getField(field.getName()); }
                            catch (FilloException e) { return ""; }
                        })
                        .collect(Collectors.toList()));

                setObjectMap.put(i, collector);
                i++;
            }

        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        return setObjectMap;
    }
    private static List<String> extractTexts(Field[] fieldsList, Recordset recordset) {
        return Stream.of(fieldsList)
                .map(field -> {
                    try {
                        return recordset.getField(field.getName());
                    } catch (FilloException e) {
                        return "";
                    }
                })
                .collect(Collectors.toList());
    }
    protected static void printError(Exception exception, String from) {
        log.error("Error " + exception + ", from " + from);
    }

}
