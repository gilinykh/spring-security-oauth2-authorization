package com.acme.oauth2;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@SpringBootConfiguration
@Import({
        ServerSecurityConfig.class,
        AuthServerOAuth2Config.class,
        OAuth2ResourceServerConfig.class
})
public class ServiceDefaultApplicationContext {

    @Bean
    @Profile({"default", "local-dev-env"})
    @ConfigurationProperties(prefix = "oauth2-data-db")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
