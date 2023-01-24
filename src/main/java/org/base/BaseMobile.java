package org.base;

import org.extensions.sql.MySqlDbExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class BaseMobile {

    @Bean public MySqlDbExtension mySqlDbExtension() { return new MySqlDbExtension(); }
}
