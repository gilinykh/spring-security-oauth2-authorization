package com.acme.oauth2;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:oauth2db;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL")
                .driverClassName("org.h2.Driver")
                .build();
    }
}