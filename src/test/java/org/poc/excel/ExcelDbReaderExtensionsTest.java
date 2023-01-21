package org.poc.excel;

import lombok.extern.slf4j.Slf4j;
import org.files.excelReader.ClassPogo;
import org.files.excelReader.ExcelDbRepository;
import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExcelDbReaderExtensionsTest {
    private ExcelDbRepository excelDbRepository;

    String path = "";

    @BeforeEach
    public void init() {
        path = "C:\\Users\\Lenovo\\IdeaProjects\\mobile.automation.extensions\\src\\test\\resources\\Book1.xlsx";
        this.excelDbRepository = new ExcelDbRepository(path);
    }

    @AfterClass
    public void tearDown() {
        this.excelDbRepository.close();
    }

    @Test
    public void readDb_printAllDbByClassPojoObject() {
        this.excelDbRepository.setQuery("select * from sheet1");

        List<ClassPogo> pogoObject = this.excelDbRepository.findByAll();
        pogoObject.forEach(obj -> log.info(obj.toString()));

        Optional<ClassPogo> classPogo = this.excelDbRepository.findBy(predicate -> predicate.getId().equals("6"));
        classPogo.ifPresent(pogo -> log.info(pogo.toString()));

         this.excelDbRepository.clear();
    }

}
