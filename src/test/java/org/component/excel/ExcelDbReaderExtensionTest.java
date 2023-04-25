package org.component.excel;

import lombok.extern.slf4j.Slf4j;
import org.extensions.anontations.excel.ExcelProvider;
import org.extensions.excel.ExcelDbReaderExtension;
import org.extensions.report.ExtentReportExtension;
import org.data.files.excelReader.ClassPogo;
import org.data.files.excelReader.ExcelDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import java.util.Optional;
@Slf4j
@ExtendWith(value = { ExtentReportExtension.class, ExcelDbReaderExtension.class })
public class ExcelDbReaderExtensionTest {

    @Test
    @ExcelProvider(fileLocation = "\\src\\test\\resources\\Book1.xlsx")
    public void readDb_printAllDbByClassPojoObject(ExcelDbRepository excelDbRepository) {
        excelDbRepository.setQuery("select * from sheet1");
        List<ClassPogo> pogoObject = excelDbRepository.findByAll();
        pogoObject.forEach(obj -> log.info(obj.toString()));
        Optional<ClassPogo> classPogo = excelDbRepository.findBy(predicate -> predicate.getId().equals("6"));
        classPogo.ifPresent(pogo -> log.info(pogo.toString()));
        excelDbRepository.clear();
    }

}
