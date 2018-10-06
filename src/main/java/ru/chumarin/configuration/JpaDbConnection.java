package ru.chumarin.configuration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Конфигурация для работы c JPA сущностями и репозиториями.
 * Может использоваться в тестах или разработке.
 * Основное назначение: В случае использования с in-memory database и hbm2ddl.auto = validate (для h2 - create) позволяет создать структуру БД
 * при старте приложения или тестов.
 */
@Configuration
@Profile("testDb")
@EnableJpaRepositories(basePackages = "ru.chumarin.database.repository",
                       entityManagerFactoryRef = "jpaEntityManagerFactory",
                       transactionManagerRef = "jpaTransactionManager")
@EnableTransactionManagement
public class JpaDbConnection extends DirectDbConnection {

    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "ru.chumarin.database.entity";

    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(@Qualifier("jpaDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.setPackagesToScan(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN);
        em.setJpaProperties(hibernateProp());
        return em;
    }

    @Bean(name = "jpaTransactionManager")
    @Primary // Только для тестовой конфигурации
    public JpaTransactionManager jpaTransactionManager(@Qualifier("jpaEntityManagerFactory") AbstractEntityManagerFactoryBean factory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(factory.getObject());
        return tm;
    }

    @Bean(name = "jpaDataSource")
    public DataSource jpaDataSource() {
        return getDataSource();
    }

    private Properties hibernateProp() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.show_sql", showSQl);
        properties.put("hibernate.hbm2ddl.auto", hbm2dll);
        properties.put("hibernate.hbm2ddl.import_files", importFiles);
        return properties;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
