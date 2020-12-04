package ru.ibs.gasu.config;

/**
 * @author erodina
 * @since 16.03.20 16:44
 */

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DbConfig {
    @Value("${db.driver.name}")
    private String driverClassName;
    @Value("${db.url}")
    private String jdbcUrl;
    @Value("${db.user.name}")
    private String userName;
    @Value("${db.user.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${db.user.schema}")
    private String dbSchema;
    @Value("${db.pool.size}")
    private int poolSize;
    @Value("${db.show_sql}")
    private String showSql;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setSchema(dbSchema);
        dataSource.setMaximumPoolSize(poolSize);
        dataSource.setMaxLifetime(300000);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        return jpaTransactionManager;
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("ru.ibs.gasu.entity");

        Properties p = new Properties();
        p.setProperty("hibernate.dialect", hibernateDialect);

        //p.setProperty("hibernate.generate_statistics", "true");

        p.setProperty("hibernate.format_sql", "true");
        p.setProperty("hibernate.show_sql", "true");
        p.setProperty("use_sql_comments", "true");

        p.setProperty("hibernate.hbm2ddl.auto", "update");
        p.setProperty("hibernate.default_schema", dbSchema);
        p.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");

        p.setProperty("hibernate.jdbc.batch_size", "50");
        p.setProperty("hibernate.jdbc.fetch_size", "50");

        p.setProperty("hibernate.jdbc.use_get_generated_keys", "true");
        p.setProperty("hibernate.physical_naming_strategy", "com.vladmihalcea.hibernate.type.util.CamelCaseToSnakeCaseNamingStrategy");
        p.setProperty("hibernate.max_fetch_depth", "1");

        emf.setJpaProperties(p);
        emf.afterPropertiesSet();
        return emf.getObject();
    }


}
