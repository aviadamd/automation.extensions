package org.base;

import org.extensions.mongo.MongoMorphiaDbExtension;
import org.extensions.pdf.PdfReaderExtension;
import org.extensions.sql.MySqlDbExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ObjectsBeans {
    @Bean
    public MySqlDbExtension mySqlDbExtension() {
        return new MySqlDbExtension();
    }
    @Bean
    public MongoMorphiaDbExtension morphiaDbExtension() {
        return new MongoMorphiaDbExtension();
    }
    @Bean
    public PdfReaderExtension pdfReaderExtension() {
        return new PdfReaderExtension();
    }
}
