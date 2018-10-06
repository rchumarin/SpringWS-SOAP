package ru.chumarin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Базовая конфигурация для чтения параметров подключения к БД.
 * Используется только для тестирования и разработки.
 */
@PropertySource("classpath:db/db.ora.properties")
public class DirectDbConnection {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.connectionUrl}")
    private String connectionUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.dialect}")
    protected String dialect;

    @Value("${db.hbm2dll}")
    protected String hbm2dll;

    @Value("${db.hbm2dll.import_files}")
    protected String importFiles;

    @Value("${db.show_sql}")
    protected String showSQl;

    protected DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }
}
