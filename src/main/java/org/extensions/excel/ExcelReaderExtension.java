package org.extensions.excel;

import org.extensions.report.ExtentReportExtension;
import org.junit.jupiter.api.extension.*;

@Extensions(value = @ExtendWith(value = ExtentReportExtension.class))
public class ExcelReaderExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback {

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }
}
