package org.utils.files.excelReader;

import com.codoid.products.fillo.Recordset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class ExcelDbRepository extends ExcelDbReader.QueryBuilder {
    private String query = "";
    private final ExcelDbReader.QueryBuilder queryBuilder;
    private static List<ClassPogo> setAllClassPojo = new ArrayList<>();
    public ExcelDbRepository(String path) {
        this.queryBuilder = new ExcelDbReader.QueryBuilder().setConnection(path);
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public Optional<ClassPogo> findBy(Predicate<ClassPogo> classPogoPredicate) {
        for (ClassPogo classPogo: this.findByAll()) {
            if (classPogoPredicate.test(classPogo)) {
                return Optional.ofNullable(classPogo);
            }
        }
        return Optional.empty();
    }
    public List<ClassPogo> findByAll() {
        if (setAllClassPojo.isEmpty()) { setAllClassPojo = getSetAllClassPojo(); }
        return setAllClassPojo;
    }

    private List<ClassPogo> getSetAllClassPojo() {
        Recordset recordset = this.queryBuilder.query(this.query).build();
        MultiValuedMap<Integer, List<String>> multiValuedMap = ExcelDbReader.queryResultMap(recordset, ClassPogo.class);

        for (Map.Entry<Integer, Collection<List<String>>> entry : multiValuedMap.asMap().entrySet()) {
            for (List<String> data: entry.getValue()) {
                setAllClassPojo.add(new ClassPogo(data.get(0), data.get(1), data.get(2)));
            }
        }

        return setAllClassPojo;
    }

    public void clear() {
        this.query = "";
        setAllClassPojo = new ArrayList<>();
    }
}
