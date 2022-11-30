package com.example.stock.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration {

    @Primary
    @Bean(name = "datasource")
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "namedLockDatasource")
    @ConfigurationProperties("named-lock.datasource.hikari")
    public HikariDataSource namedLockDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
