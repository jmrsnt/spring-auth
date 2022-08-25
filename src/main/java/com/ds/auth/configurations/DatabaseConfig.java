package com.ds.auth.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ds.auth")
public class DatabaseConfig {
    DataSource dataSource;
    JpaProperties jpaProperties;

    public DatabaseConfig(DataSource dataSource, JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
        this.dataSource = dataSource;
    }

    @Bean(name = "transactionManager")
    PlatformTransactionManager transactionManager(
        @Qualifier("entityManagerFactory")
        EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setDataSource(dataSource);
        em.setPersistenceUnitName("entityManagerFactory");
        em.setJpaDialect(new HibernateJpaDialect());
        em.setPackagesToScan("com.ds.auth.entities");
        em.setJpaPropertyMap(jpaProperties.getProperties());
        return em;
    }
}
