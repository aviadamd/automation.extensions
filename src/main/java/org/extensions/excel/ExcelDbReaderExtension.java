package org.extensions.excel;

import org.extensions.anontations.excel.ExcelProvider;
import org.data.files.excelReader.ExcelDbRepository;
import org.junit.jupiter.api.extension.*;

public class ExcelDbReaderExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final ThreadLocal<ExcelDbRepository> excelDbRepository = new ThreadLocal<>();

    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == ExcelDbRepository.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) {
        return this.excelDbRepository.get();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            ExcelProvider provider = context.getElement().get().getAnnotation(ExcelProvider.class);
            String userDir = System.getProperty("user.dir");
            this.excelDbRepository.set(new ExcelDbRepository(userDir + "/" + provider.fileLocation()));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (context.getElement().isPresent()) {
            this.excelDbRepository.get().close();
        }
    }
}
