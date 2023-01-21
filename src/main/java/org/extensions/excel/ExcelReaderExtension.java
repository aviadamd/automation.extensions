package org.extensions.excel;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExcelReaderExtension implements
        BeforeAllCallback,
        BeforeEachCallback,
        AfterAllCallback {

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
